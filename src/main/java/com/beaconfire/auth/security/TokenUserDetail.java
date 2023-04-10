package com.beaconfire.auth.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenUserDetail {

    private int user_id;
    private String email;
    private String username;
}
