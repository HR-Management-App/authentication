package com.beaconfire.auth.domain.exception;

public class RoleNotFoundByNameException extends Exception{

    public RoleNotFoundByNameException(String message) {
        super(message);
    }
}
