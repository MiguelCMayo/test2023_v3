package com.plexus.test2023.infrastructure.controllers;

import com.plexus.test2023.application.services.PriceService;
import com.plexus.test2023.domain.dto.ApiResponseDTO;
import com.plexus.test2023.domain.dto.PriceDTO;
import com.plexus.test2023.infrastructure.exception.PriceCreationException;
import com.plexus.test2023.infrastructure.exception.TariffNotFoundException;
import com.plexus.test2023.infrastructure.models.Price;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prices")
public class PriceController {
    private final PriceService priceService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Price>> createPrice(@RequestBody Price price) {
        if (!"1".equals(price.getBrand().getBrandId())) {
            throw new PriceCreationException("The brandId is not found in the database");
        }

        try {
            Price createdPrice = priceService.createPrice(price);

            ApiResponseDTO<Price> response = new ApiResponseDTO<>();
            response.setSuccess(true);
            response.setMessage("Price created successfully");
            response.setData(createdPrice);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PriceCreationException ex) {
            ApiResponseDTO<Price> response = new ApiResponseDTO<>();
            response.setSuccess(false);
            response.setMessage("Error creating price: " + ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PriceDTO>>> getAllPrices() {
        List<PriceDTO> prices = priceService.getAllPrices();

        ApiResponseDTO<List<PriceDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Price list successfully recovered");
        response.setData(prices);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDTO<PriceDTO>> searchPriceById(@PathVariable("id") Long id) {
        try {
            PriceDTO price = priceService.getPriceById(id);

            if (price != null) {
                ApiResponseDTO<PriceDTO> response = new ApiResponseDTO<>();
                response.setSuccess(true);
                response.setMessage("Price successfully found");
                response.setData(price);
                return ResponseEntity.ok(response);
            } else {
                ApiResponseDTO<PriceDTO> response = new ApiResponseDTO<>();
                response.setSuccess(false);
                response.setMessage("Price not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (TariffNotFoundException ex) {
            ApiResponseDTO<PriceDTO> response = new ApiResponseDTO<>();
            response.setSuccess(false);
            response.setMessage("Price not found exception");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deletePriceById(@PathVariable("id") Long id) {
        priceService.deletePriceById(id);

        ApiResponseDTO<Void> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Price successfully removed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find_prices_by_criterion")
    public ResponseEntity<ApiResponseDTO<List<PriceDTO>>>
    findPricesByCriterion(
            @RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") String brandId
    ) {
        List<PriceDTO> prices = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        ApiResponseDTO<List<PriceDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Prices successfully found");
        response.setData(prices);

        return ResponseEntity.ok(response);
    }
}
