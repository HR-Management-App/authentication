package com.beaconfire.auth.AOP;

import com.beaconfire.auth.domain.exception.RoleNotFoundByNameException;
import com.beaconfire.auth.domain.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleException(Exception e) {
        return new ResponseEntity(GeneralResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build(),
                HttpStatus.OK);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity(GeneralResponse.builder()
                .success(false)
                .message("Provided credential is invalid.")
                .build(),
                HttpStatus.OK);
    }

    @ExceptionHandler(value = {RoleNotFoundByNameException.class})
    public ResponseEntity handleRoleNotFoundByNameException(RoleNotFoundByNameException e) {
        return new ResponseEntity(GeneralResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build(),
                HttpStatus.OK);
    }
}
