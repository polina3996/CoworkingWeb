package coworking.controller;

import coworking.config.JwtUtil;
import coworking.dto.RegisterRequest;
import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import coworking.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (this.userEntityRepository.findByName(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        String role = (request.getRole() != null && !request.getRole().isEmpty()) ? "ROLE_" + request.getRole() : "ROLE_USER";

        UserEntity user = new UserEntity(
                request.getUsername(),
                this.passwordEncoder.encode(request.getPassword()),
                role
        );

        this.userEntityRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = this.userEntityRepository.findByName(request.getUsername());

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        final String jwt = this.jwtUtil.generateToken(new User(
                user.getName(),
                user.getPassword(),
                java.util.Collections.singletonList(authority)
        ));
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
