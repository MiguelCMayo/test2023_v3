package com.plexus.test2023.infrastructure.exception;

import com.plexus.test2023.domain.dto.ApiResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PriceCreationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handlePriceCreationException(PriceCreationException ex) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setMessage("Error in price creation: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
