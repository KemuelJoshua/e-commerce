package com.kemueljoshuamariano.ecommerce.auth.service;

import com.kemueljoshuamariano.ecommerce.auth.model.LoginRequest;
import com.kemueljoshuamariano.ecommerce.common.response.Response;


public interface AuthenticationService {

    Response authenticateUser(LoginRequest loginRequest);

}
