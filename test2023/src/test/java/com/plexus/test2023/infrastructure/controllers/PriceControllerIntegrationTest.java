package com.plexus.test2023.infrastructure.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plexus.test2023.application.services.PriceService;
import com.plexus.test2023.domain.dto.ApiResponseDTO;
import com.plexus.test2023.domain.dto.PriceDTO;
import com.plexus.test2023.infrastructure.exception.TariffNotFoundException;
import com.plexus.test2023.infrastructure.models.Brand;
import com.plexus.test2023.infrastructure.models.Price;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PriceService priceService;

    @Test
    public void testCreatePrice_WhenBrandIdEqualTo1_ThenReturnStatusCreated() throws Exception {
        Price price = new Price();
        Brand brand = new Brand();
        brand.setBrandId("1");
        price.setBrand(brand);

        // Send a POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/prices", price, String.class);

        // Check the HTTP status
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // Parse the response JSON to ApiResponseDTO
        ApiResponseDTO<Price> response = objectMapper.readValue(responseEntity.getBody(), new TypeReference<ApiResponseDTO<Price>>() {});

        // Check if the response indicates success
        assertTrue(response.isSuccess());

        // Check if the response message is correct
        assertEquals("Price created successfully", response.getMessage());
    }

    @Test
    public void testCreatePrice_WhenBrandIdDistinctTo1_ThenReturnStatusBadRequest() throws Exception {
        Price price = new Price();
        Brand brand = new Brand();
        brand.setBrandId("6");
        price.setBrand(brand);

        // Send a POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/prices", price, String.class);

        // Check the HTTP status
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Parse the response JSON to ApiResponseDTO
        ApiResponseDTO<Price> response = objectMapper.readValue(responseEntity.getBody(), new TypeReference<ApiResponseDTO<Price>>() {});

        // Check if the response indicates success
        assertFalse(response.isSuccess());

        // Check if the response message is correct
        assertEquals("Error in price creation: The brandId is not found in the database", response.getMessage());
    }

    @Test
    public void testGetAllPrices() throws Exception {
        List<PriceDTO> mockPrices = new ArrayList<>();
        for (long i = 1; i <= 4; i++) {
            PriceDTO price = new PriceDTO();
            price.setId(i);
            mockPrices.add(price);
        }

        Mockito.when(priceService.getAllPrices()).thenReturn(mockPrices);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());

                    ApiResponseDTO<List<PriceDTO>> response = objectMapper.readValue(jsonResponse, new TypeReference<ApiResponseDTO<List<PriceDTO>>>() {});

                    assertTrue(response.isSuccess());
                    assertEquals("Price list successfully recovered", response.getMessage());

                    List<PriceDTO> prices = response.getData();

                    assertEquals(4, prices.size());
                });
    }

    @Test
    public void testSearchPriceById_WhenPriceFound() throws Exception {
        Long priceId = 1L;
        PriceDTO mockPrice = new PriceDTO();
        mockPrice.setId(priceId);

        Mockito.when(priceService.getPriceById(priceId)).thenReturn(mockPrice);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices/" + priceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Price successfully found"))
                .andExpect(jsonPath("$.data.id").value(priceId));
    }

    @Test
    public void testSearchPriceById_WhenPriceNotFound() throws Exception {
        Long priceId = 2L;

        Mockito.when(priceService.getPriceById(priceId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices/" + priceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Price not found"));
    }

    @Test
    public void testSearchPriceById_WhenPriceNotFoundException() throws Exception {
        Long priceId = 3L;

        Mockito.when(priceService.getPriceById(priceId)).thenThrow(new TariffNotFoundException(""));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices/" + priceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Price not found exception"));
    }

    @Test
    public void testDeletePriceById_WhenPriceSuccessfullyRemoved() throws Exception {
        Long priceId = 1L;

        Mockito.doNothing().when(priceService).deletePriceById(priceId);

        mockMvc.perform(delete("/prices/" + priceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Price successfully removed"));
    }

    @Test
    public void testFindPricesByCriterion() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.now();
        Long productId = 1L;
        String brandId = "1";

        List<PriceDTO> mockPrices = new ArrayList<>();
        PriceDTO price1 = new PriceDTO();
        price1.setId(1L);
        price1.setBrandId("1");

        mockPrices.add(price1);

        Mockito.when(priceService.findPricesByCriterion(applicationDate, productId, brandId)).thenReturn(mockPrices);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices/find_prices_by_criterion")
                        .param("applicationDate", applicationDate.toString())
                        .param("productId", productId.toString())
                        .param("brandId", brandId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Prices successfully found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].brandId").value(brandId));
    }
}
