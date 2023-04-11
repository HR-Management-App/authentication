package com.beaconfire.auth.domain.request;


import com.beaconfire.auth.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class HousingRequest implements Serializable {

    private String type;

    private User user;

}
