package com.beaconfire.auth.controller;

import com.beaconfire.auth.domain.exception.TokenNotFoundException;
import com.beaconfire.auth.domain.exception.TokenNotValidException;
import com.beaconfire.auth.domain.response.GeneralResponse;
import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.exception.RoleNotFoundByNameException;
import com.beaconfire.auth.domain.request.SignupRequest;
import com.beaconfire.auth.service.TokenService;
import com.beaconfire.auth.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SignupController {

    private final UserService userService;

    private final TokenService tokenService;

    private RabbitTemplate rabbitTemplate;

    public SignupController(UserService userService, TokenService tokenService) {

        this.userService = userService;
        this.tokenService = tokenService;

    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/signup/{token}")
    @ResponseBody
    public GeneralResponse submitSignup(@Valid @RequestBody SignupRequest request,
                                        @PathVariable String token,
                                        BindingResult result) throws RoleNotFoundByNameException, TokenNotValidException, TokenNotFoundException {
        if (result.hasErrors()) return GeneralResponse.builder()
                .success(false)
                .message("Something went wrong in binding//")
                .build();

        tokenService.validateToken(token, request.getEmail()); // if there's something wrong, exception would be called and process by AOP

        if (! userService.validateNewUser(request.getUsername(), request.getEmail())) {
            return GeneralResponse.builder()
                    .success(false)
                    .message("duplicated username or email")
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        userService.registration(user);

        return GeneralResponse.builder()
                .success(true)
                .message("Signup success")
                .build();

    }
}
