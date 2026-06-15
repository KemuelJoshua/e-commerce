package com.kemueljoshuamariano.ecommerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String errorMessage;
    private int errorCode;

}
