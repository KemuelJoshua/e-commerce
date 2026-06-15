package com.kemueljoshuamariano.ecommerce.config;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {

            User user = new User();

            user.setProvider("LOCAL");
            user.setProviderId("admin");
            user.setUsername("admin");
            user.setPassword(this.passwordEncoder.encode("password"));
            user.setFirstname("System");
            user.setLastname("Administrator");
            user.setEmail("admin@example.com");
            user.setRole("ADMIN");
            user.setIsActive(true);

            userRepository.save(user);

            System.out.println("Default admin account created.");
        }
    }
}
