package com.tripapi.dto.Auth;

import lombok.AllArgsConstructor;
import com.tripapi.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private LocalDate dob;

    public static UserDTO fromEntity(User u) {
        if (u == null) return null;
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getEmail(),
                u.getDob()
        );
    }
}