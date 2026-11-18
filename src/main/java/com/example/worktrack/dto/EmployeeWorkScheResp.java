package com.example.worktrack.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkScheResp  extends EmployeeWorkScheEntyResponse{
    private String positionName;
}
