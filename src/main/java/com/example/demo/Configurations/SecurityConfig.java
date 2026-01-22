package com.example.demo.Configurations;

import com.example.demo.Filter.JwtFilter;
import com.example.demo.Services.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private MyUserDetailService myUserDetailService;
    private JwtFilter jwtFilter;

    public SecurityConfig(MyUserDetailService userDetailService, JwtFilter jwtFilter){
        this.myUserDetailService=userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http){
        http.csrf(customize -> customize.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/signUp","/logIn")
                        .permitAll().anyRequest()
                        .authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager createManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(myUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
