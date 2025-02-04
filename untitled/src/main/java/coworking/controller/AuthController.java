package coworking.controller;

import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        if (userEntityRepository.findByName(user.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword())); // Encrypt password
        user.setRole(user.getRole());
        this.userEntityRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
