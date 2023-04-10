package com.beaconfire.auth.domain.exception;


public class TokenNotValidException extends Exception{
    public TokenNotValidException(String message) {
        super(message);
    }
}
