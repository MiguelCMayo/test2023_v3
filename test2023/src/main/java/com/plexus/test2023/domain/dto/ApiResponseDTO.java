package com.plexus.test2023.domain.dto;

import lombok.Data;

@Data
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
}


