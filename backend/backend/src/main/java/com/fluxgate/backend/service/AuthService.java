package com.fluxgate.backend.service;

import com.fluxgate.backend.dto.LoginRequest;
import com.fluxgate.backend.dto.SignUpRequest;
import com.fluxgate.backend.entity.Role;
import com.fluxgate.backend.entity.User;
import com.fluxgate.backend.repository.UserRepository;
import com.fluxgate.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // SignUp
    public User register(SignUpRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists!!!!");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    //Login
   public String login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found!!!"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Credentials!!!");
        }
        return jwtUtil.generateToken(user.getEmail());
   }
}