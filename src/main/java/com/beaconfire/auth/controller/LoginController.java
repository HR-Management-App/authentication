package com.beaconfire.auth.controller;

import com.beaconfire.auth.domain.request.LoginRequest;
import com.beaconfire.auth.domain.response.GeneralResponse;
import com.beaconfire.auth.security.AuthUserDetail;
import com.beaconfire.auth.security.JwtProvider;
import com.beaconfire.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
    }

    private JwtProvider jwtProvider;

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    //User trying to log in with username and password
    @PostMapping("/login")
    @ResponseBody
    public GeneralResponse login(@Valid @RequestBody LoginRequest request) {

        System.out.println("GO TO LOGIN CONTROLLER");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal();

//        System.out.println("AuthUserDetail username: " + authUserDetail.getUsername());
//        System.out.println("AuthUserDetail user_id: " + authUserDetail.getUser_id());
//        System.out.println("AuthUserDetail email: " + authUserDetail.getEmail());
//        System.out.println("authUserDetail authorities:");
//        System.out.println(authUserDetail.getAuthorities());

        String token = jwtProvider.createToken(authUserDetail);

        return GeneralResponse.builder()
                .success(true)
                .message("Welcome " + authUserDetail.getUsername())
                .data(token)
                .build();
    }



}
