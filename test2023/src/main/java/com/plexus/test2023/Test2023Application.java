package com.plexus.test2023;

import com.plexus.test2023.domain.dto.ApiResponseDTO;
import com.plexus.test2023.domain.dto.PriceDTO;
import com.plexus.test2023.infrastructure.controllers.PriceController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.plexus"})
public class Test2023Application {

    private static ApplicationContext applicationContext;

    @Autowired
    public Test2023Application(ApplicationContext applicationContext) {
        Test2023Application.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(Test2023Application.class, args);

        log.info("Searching valid price");
        LocalDateTime validApplicationDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        Long productId = 35455L;
        String brandId = "1";

        PriceController priceController = applicationContext.getBean(PriceController.class);

        ResponseEntity<ApiResponseDTO<List<PriceDTO>>> responseEntity = priceController.findPricesByCriterion(validApplicationDate, productId, brandId);

        List<PriceDTO> validPrice = responseEntity.getBody().getData();

        for (PriceDTO searchedPrice : validPrice) {
            System.out.println("Price found: " + searchedPrice);
        }

        log.info("Searching price that does not exist");
        LocalDateTime invalidApplicationDate = LocalDateTime.of(2023, 6, 14, 15, 0);

        ResponseEntity<ApiResponseDTO<List<PriceDTO>>> responseEntity2 = priceController.findPricesByCriterion(invalidApplicationDate, productId, brandId);

        List<PriceDTO> invalidPrice = responseEntity2.getBody().getData();

        for (PriceDTO searchedPrice : invalidPrice) {
            System.out.println("Price found: " + searchedPrice);
        }

        log.info("Searching valid price again from cache");
        for (PriceDTO searchedPrice : validPrice) {
            System.out.println("Price found: " + searchedPrice);
        }

        log.info("Searching all prices");

        ResponseEntity<ApiResponseDTO<List<PriceDTO>>> responseEntity3 = priceController.getAllPrices();

        List<PriceDTO> foundPrices4 = responseEntity3.getBody().getData();

        for (PriceDTO searchedPrice : foundPrices4) {
            System.out.println("All prices found: " + searchedPrice);
        }
    }
}
