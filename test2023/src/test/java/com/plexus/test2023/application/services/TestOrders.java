package com.plexus.test2023.application.services;

import com.plexus.test2023.domain.dto.PriceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestOrders {

    @Autowired
    private PriceService priceService;

    @Test
    public void testFindPricesByCriterion_Price1() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        String brandId = "1";

        List<PriceDTO> priceDTOs = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(1, priceDTOs.size());
    }

    @Test
    public void testFindPricesByCriterion_Price2() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        String brandId = "1";

        List<PriceDTO> priceDTOs = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(1, priceDTOs.size());
    }

    @Test
    public void testFindPricesByCriterion_Price3() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0);
        Long productId = 35455L;
        String brandId = "1";

        List<PriceDTO> priceDTOs = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(1, priceDTOs.size());
    }

    @Test
    public void testFindPricesByCriterion_Price4() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);
        Long productId = 35455L;
        String brandId = "1";

        List<PriceDTO> priceDTOs = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(1, priceDTOs.size());
    }

    @Test
    public void testFindPricesByCriterion_Price5() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0);
        Long productId = 35455L;
        String brandId = "1";

        List<PriceDTO> priceDTOs = priceService.findPricesByCriterion(applicationDate, productId, brandId);

        assertEquals(1, priceDTOs.size());
    }
}
