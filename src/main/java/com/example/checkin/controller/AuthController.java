package com.example.checkin.controller;

import com.example.checkin.jwt.JwtUtil;
import com.example.checkin.models.Users;
import com.example.checkin.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // optional: allows frontend requests
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok().body("Bearer " + token);
    }

    @GetMapping("/verify-token")
    public  ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("Unauthorized");
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.getClaims(token);
            String role = (String) claims.get("role");

            return ResponseEntity.ok().body("role: " + role);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Token");

        }
    }
}
