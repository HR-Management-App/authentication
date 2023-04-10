package com.beaconfire.auth.domain.exception;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
