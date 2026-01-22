package com.example.demo.Services;

import com.example.demo.Entities.Users;
import com.example.demo.Exceptions.UserException;
import com.example.demo.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository repo;
    AuthenticationManager authManager;
    PasswordEncoder encoder;
    UserService(UserRepository repo, AuthenticationManager authManager, PasswordEncoder encoder){
        this.repo = repo;
        this.authManager = authManager;
        this.encoder = encoder;
    }
    public void createUser(String username, String password){
        if(this.repo.existsByUsername(username)){
            throw new UserException("User already exist");
        }
        Users user = new Users(username,encoder.encode(password));
        this.repo.save(user);
    }
    public boolean verity(String username, String password){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        return auth.isAuthenticated();
    }
}
