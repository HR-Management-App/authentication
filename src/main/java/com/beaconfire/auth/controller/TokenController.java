package com.beaconfire.auth.controller;


import com.beaconfire.auth.domain.entity.RegistrationToken;
import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.request.TokenRequest;
import com.beaconfire.auth.domain.response.GeneralResponse;
import com.beaconfire.auth.security.TokenUserDetail;
import com.beaconfire.auth.service.TokenService;
import com.beaconfire.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("token")
public class TokenController {

    private TokenService tokenService;

    private UserService userService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setUserService(UserService userService) {this.userService = userService;}


    @GetMapping("/test")
    public Object getAuthUserDetail(){
        System.out.println("In token Controller /token/test");
        System.out.println("user_id: " + ((TokenUserDetail)SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser_id());
        System.out.println("email: " + ((TokenUserDetail)SecurityContextHolder.getContext().getAuthentication().getDetails()).getEmail());
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/all")
    public GeneralResponse getAllTokens() {
        List<RegistrationToken> registrationTokenList = tokenService.getAllToken();

        return GeneralResponse.builder()
                .success(true)
                .data(registrationTokenList)
                .build();
    }

//    @GetMapping
//    public void getTokenPage() {
//        //todo
//    }


    @PostMapping("/new")
    public GeneralResponse createNewToken(@RequestBody TokenRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return GeneralResponse.builder()
                    .success(false)
                    .message("Something went wrong in binding")
                    .build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User hr = userService.getUserByUsername(username);

        RegistrationToken newToken = tokenService.createNewRegistrationToken(hr, request.getEmail());

        return GeneralResponse.builder()
                .success(true)
                .message("Registration Token created && Registration Email sent")
                .data(newToken)
                .build();
    }
}
