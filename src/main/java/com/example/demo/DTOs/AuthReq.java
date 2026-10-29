package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthReq(@NotBlank @Size(min = 3, max = 50) String username,
                      @NotBlank @Size(min = 8, max = 100) String password) {
}
