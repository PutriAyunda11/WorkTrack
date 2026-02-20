package com.example.worktrack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private T data;

}
