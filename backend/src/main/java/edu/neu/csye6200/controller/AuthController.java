package edu.neu.csye6200.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.csye6200.dto.LoginDTO;
import edu.neu.csye6200.dto.UserRegisterDTO;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.security.JwtUtil;
import edu.neu.csye6200.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> auth(@RequestBody LoginDTO loginDTO) {
        try {
            boolean isAuthenticated = authService.authenticate(loginDTO);
            if (isAuthenticated) {
                String token = jwtUtil.generateToken(loginDTO.getEmail());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Authentication failed");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            User registeredUser = authService.register(userRegisterDTO);
            return ResponseEntity.ok("User registered with ID: " + registeredUser.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
