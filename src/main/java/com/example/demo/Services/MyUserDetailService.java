package com.example.demo.Services;

import com.example.demo.Entities.Users;
import com.example.demo.Exceptions.UserException;
import com.example.demo.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {
    private UserRepository userRepository;
    MyUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users curr = this.userRepository.findByUsername(username);
        if(curr == null){
            throw new UserException("User not found");
        }
        return new UserPrincipal(curr);
    }
}
