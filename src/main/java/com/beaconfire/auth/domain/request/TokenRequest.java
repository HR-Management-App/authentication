package com.beaconfire.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenRequest {


    @NotBlank(message = "email cannot be blank")
    private String email;



}
