package com.kemueljoshuamariano.ecommerce.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kemueljoshuamariano.ecommerce.common.exception.Error;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Response {

    private final String status;

    protected Response(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Object getPayload() {
        return null;
    }

    public Error getError() {
        return null;
    }
}
