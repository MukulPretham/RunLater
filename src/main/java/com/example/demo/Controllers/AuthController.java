package com.example.demo.Controllers;

import com.example.demo.DTOs.AuthReq;
import com.example.demo.DTOs.AuthResponse;
import com.example.demo.Exceptions.UserException;
import com.example.demo.Services.JwtService;
import com.example.demo.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    UserService userService;
    JwtService jwtService;
    AuthController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUp(@RequestBody AuthReq req){
        userService.createUser(req.username(), req.password());
        return ResponseEntity.status(200).body(new AuthResponse("Account Created",""));
    }
    @PostMapping("/logIn")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthReq req){
        if(userService.verity(req.username(),req.password())){
            String token = jwtService.generate(req.username());
            return ResponseEntity.status(200).body(new AuthResponse("Authenticated",token));
        }else {
            throw new UserException("Authentication Failed");
        }
    }

    @GetMapping("authTest")
    public ResponseEntity<String> test(Authentication auth){
        UserDetails userDetails =
                (UserDetails) auth.getPrincipal();
        return ResponseEntity.status(200).body(userDetails.getUsername());
    }
}
