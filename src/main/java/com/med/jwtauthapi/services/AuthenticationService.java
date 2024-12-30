package com.med.jwtauthapi.services;

import com.med.jwtauthapi.dtos.LoginUserDto;
import com.med.jwtauthapi.dtos.RegisterUserDto;
import com.med.jwtauthapi.responses.CustomUser;
import com.med.jwtauthapi.entities.Customer;
import com.med.jwtauthapi.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public CustomUser signup(RegisterUserDto input) {
        Customer customer = Customer.builder()
                .email(input.getEmail())
                .password(
                        passwordEncoder.encode(input.getPassword()))
                .fullName(input.getFullName())
                .build();
        customer = customerRepository.save(customer);
        return CustomUser.builder().customer(customer).build();
    }

    public CustomUser authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        Customer customer = customerRepository.findByEmail(
                input.getEmail()).orElseThrow();
        return CustomUser.builder().customer(customer).build();
    }

}
