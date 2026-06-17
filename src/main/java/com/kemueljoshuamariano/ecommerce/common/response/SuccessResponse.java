package com.kemueljoshuamariano.ecommerce.common.response;

public class SuccessResponse extends Response {

    private final Object payload;

    public SuccessResponse(Object payload) {
        super("success");
        this.payload = payload;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
