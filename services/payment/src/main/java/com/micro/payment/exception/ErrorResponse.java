package com.micro.payment.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
        ) {

}
