package com.ecommerce.config;

import com.ecommerce.entity.User;
import com.ecommerce.enums.Role;
import com.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserIntializer {

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                // Using the Lombok Builder to construct the user
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin1234")) // Still ensuring the password is encrypted!
                        .role(Role.ADMIN) // Using uppercase ADMIN is standard convention
                        .build();

                userRepository.save(user);
                System.out.println("Admin user is created");
            }
            if (userRepository.findByUsername("user").isEmpty()) {

                // Using the Lombok Builder to construct the user
                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user1234")) // Still ensuring the password is encrypted!
                        .role(Role.USER) // Using uppercase ADMIN is standard convention
                        .build();

                userRepository.save(user);
                System.out.println("normal user is created");
            }
        };
    }}