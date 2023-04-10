package com.beaconfire.auth.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class EmailRequest implements Serializable {
    private String destination;
    private String subject;
    private String content;
}
