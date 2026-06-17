package com.kemueljoshuamariano.ecommerce.common.response;

import com.kemueljoshuamariano.ecommerce.common.exception.Error;

public class ErrorResponse extends Response {

    private final String message;
    private final Error error;

    public ErrorResponse(String message, Error error) {
        super("failed");
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(String message, String details, int code) {
        this(message, new Error(details, code));
    }

    public ErrorResponse(String message, int code) {
        this(message, message, code);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Error getError() {
        return error;
    }
}
