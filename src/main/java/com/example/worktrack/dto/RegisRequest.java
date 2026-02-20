package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisRequest {
    private String email;
    private String Position;
    private String usename;
    private String password;
}
