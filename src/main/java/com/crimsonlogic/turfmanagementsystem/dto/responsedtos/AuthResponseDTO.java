package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;

import lombok.Data;
import java.util.List;

@Data
public class AuthResponseDTO {
    private String token;
    private String userId;
    private String name;
    private String email;
    private List<String> roles;

    public AuthResponseDTO(String token, String userId, String name, String email, List<String> roles) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }
}
