package com.kemueljoshuamariano.ecommerce.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);
}
