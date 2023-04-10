package com.beaconfire.auth.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneralResponse {
    private Boolean success;
    private String message;
    private Object data;
}
