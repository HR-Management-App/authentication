package com.beaconfire.auth.util;

import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.request.EmailRequest;
import com.beaconfire.auth.domain.request.HousingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializeUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeEmailRequest(EmailRequest message){

        String result = null;

        try {
            result = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String serializeHousingRequest(HousingRequest message){

        String result = null;

        try {
            result = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
