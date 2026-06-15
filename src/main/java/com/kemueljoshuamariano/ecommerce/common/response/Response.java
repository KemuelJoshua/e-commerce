package com.kemueljoshuamariano.ecommerce.common.response;

import com.kemueljoshuamariano.ecommerce.common.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String status;
    private Object payload;
    private Error error;

    public Response(String status, Error error) {
        this.status = status;
        this.error = error;
    }
}
