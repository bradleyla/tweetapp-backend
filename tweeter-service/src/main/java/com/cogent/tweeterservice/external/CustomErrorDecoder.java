package com.cogent.tweeterservice.external;

import com.cogent.tweeterservice.exception.CustomRuntimeException;
import com.cogent.tweeterservice.payload.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper om = new ObjectMapper();
        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try {
            ErrorResponse errorResponse = om.readValue(response.body()
                    .asInputStream(), ErrorResponse.class);
            return new CustomRuntimeException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());

        } catch (IOException e) {
            throw new CustomRuntimeException("Internal Server Error", "INTERNAL-SERVER-ERROR", 500);
        }

    }
}
