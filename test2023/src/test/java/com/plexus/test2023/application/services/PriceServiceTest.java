package com.plexus.test2023.application.services;

import com.plexus.test2023.domain.dto.PriceDTO;
import com.plexus.test2023.infrastructure.exception.TariffNotFoundException;
import com.plexus.test2023.infrastructure.models.Price;
import com.plexus.test2023.infrastructure.repositories.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ModelMapper modelMapper;

    private PriceService priceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        priceService = new PriceService(priceRepository, modelMapper);
    }

    @Test
    public void testGetPriceById() throws TariffNotFoundException {
        Long id = 1L;
        Price price = new Price();
        PriceDTO expectedPriceDTO = new PriceDTO();

        Mockito.when(priceRepository.findById(id)).thenReturn(Optional.of(price));

        PriceDTO result = priceService.getPriceById(id);

        assertEquals(expectedPriceDTO, result);
    }

    @Test
    public void testGetPriceByIdNotFound() {
        Long id = 2L;

        Mockito.when(priceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TariffNotFoundException.class, () -> priceService.getPriceById(id));
    }

    @Test
    public void testGetAllPrices() {
        List<Price> priceList = List.of(new Price(), new Price(), new Price());
        List<PriceDTO> expectedPriceDTOList = priceList.stream()
                .map(price -> new PriceDTO())
                .collect(Collectors.toList());

        Mockito.when(priceRepository.findAll()).thenReturn(priceList);

        Mockito.when(modelMapper.map(Mockito.any(Price.class), Mockito.eq(PriceDTO.class))).thenReturn(new PriceDTO());

        List<PriceDTO> result = priceService.getAllPrices();

        assertEquals(expectedPriceDTOList, result);

        assertEquals(3, result.size());
    }

    @Test
    public void testFindPricesByCriterion_PriceFound() {
        LocalDateTime applicationDate = LocalDateTime.now();
        Long productId = 1L;
        String brandId = "1";
        Price price = new Price();
        PriceDTO expectedPriceDTO = new PriceDTO();

        Mockito.when(priceRepository.findSelectedPrice(applicationDate, productId, brandId)).thenReturn(Optional.of(price));

        Mockito.when(modelMapper.map(price, PriceDTO.class)).thenReturn(expectedPriceDTO);

        List<PriceDTO> result = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(Collections.singletonList(expectedPriceDTO), result);
    }

    @Test
    public void testFindPricesByCriterion_PriceNotFound() {
        LocalDateTime applicationDate = LocalDateTime.now();
        Long productId = 1L;
        String brandId = "1";

        Mockito.when(priceRepository.findSelectedPrice(applicationDate, productId, brandId)).thenReturn(Optional.empty());

        List<PriceDTO> result = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertTrue(result.isEmpty());
    }
}