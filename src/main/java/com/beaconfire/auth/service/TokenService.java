package com.beaconfire.auth.service;

import com.beaconfire.auth.dao.RegistrationTokenDao;
import com.beaconfire.auth.dao.UserDao;
import com.beaconfire.auth.domain.entity.RegistrationToken;
import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.exception.TokenNotFoundException;
import com.beaconfire.auth.domain.exception.TokenNotValidException;
import com.beaconfire.auth.domain.request.EmailRequest;
import com.beaconfire.auth.util.SerializeUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class TokenService {

    RegistrationTokenDao registrationTokenDao;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRegistrationTokenDao(RegistrationTokenDao registrationTokenDao) {
        this.registrationTokenDao = registrationTokenDao;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public RegistrationToken createNewRegistrationToken(User hr, String email) {
        RegistrationToken newToken = addToken(hr, email);

        sendEmailRequest(newToken);

        return newToken;
    }

    private RegistrationToken addToken(User hr, String email) {

        String tokenString;
        do {
            tokenString = generateRandomToken();
        } while (registrationTokenDao.findByTokenString(tokenString).isPresent());

        RegistrationToken newToken = RegistrationToken.builder()
                .token(tokenString)
                .email(email)
                .expirationDate(new Date(System.currentTimeMillis() + 10800000)) //3 * 60 * 60 * 1000 = 10800000 -> 3 hours
                .createBy(hr)
                .build();

        int newTokenId = registrationTokenDao.addToken(newToken);

        return registrationTokenDao.findById(newTokenId);
    }

    private String generateRandomToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 40;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println("GeneratedRandomToken: " + generatedString);

        return generatedString;
    }

    private void sendEmailRequest(RegistrationToken token) {
        String destinationEmail = token.getEmail();
        String subject = String.format("Registration Link for %s to HR System", token.getEmail());
        String content = String.format("Registration link \n Email: %s \n Registration Link: http://localhost:7070/signup/%s", token.getEmail(), token.getToken());

        System.out.println("Destination Email is " + destinationEmail);
        System.out.println("Subject is: " + subject);
        System.out.println("Content is: " + content);

        EmailRequest emailRequest = EmailRequest.builder()
                .destination(destinationEmail)
                .subject(subject)
                .content(content)
                .build();

        String jsonMessage = SerializeUtil.serialize(emailRequest);

        System.out.println("JsonMessage goes to Email Service is " + jsonMessage);

        rabbitTemplate.convertAndSend("authenticationExchange", "email", jsonMessage);
    }

    @Transactional
    public List<RegistrationToken> getAllToken() {
        return registrationTokenDao.getAllToken();
    }

    @Transactional
    public boolean validateToken(String tokenString, String email) throws TokenNotValidException {
        Optional<RegistrationToken> tokenOptional = registrationTokenDao.findByTokenString(tokenString);
        if (!tokenOptional.isPresent()) {
            throw new TokenNotValidException("Token is not found in database");
        }
        RegistrationToken token = tokenOptional.get();
//        if (token.getExpirationDate().after(new Date(System.currentTimeMillis())) || token.getEmail() != email) {
//            throw new TokenNotValidException("Token is not valid");
//        }
        if (token.getExpirationDate().before(new Date(System.currentTimeMillis()))) {
            throw new TokenNotValidException("Token has expired");
        }
        if (!token.getEmail().equals(email)) {
            throw new TokenNotValidException("Token is for a different email address");
        }

        return true;

    }



}
