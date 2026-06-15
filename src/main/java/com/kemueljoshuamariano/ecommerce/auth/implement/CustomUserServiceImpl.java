package com.kemueljoshuamariano.ecommerce.auth.implement;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import com.kemueljoshuamariano.ecommerce.user.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public CustomUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())))
                .disabled(Boolean.FALSE.equals(user.getIsActive()))
                .build();
    }
}
