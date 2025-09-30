package com.tripapi.dto.Auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String dob; // YYYY-MM-DD
}