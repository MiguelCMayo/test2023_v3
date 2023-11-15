package com.plexus.test2023.application.services;

import com.plexus.test2023.domain.dto.PriceDTO;
import com.plexus.test2023.infrastructure.exception.TariffNotFoundException;
import com.plexus.test2023.infrastructure.models.Price;
import com.plexus.test2023.infrastructure.repositories.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PriceService {
    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;

    public Price createPrice(Price price) {
        return priceRepository.save(price);
    }

    public PriceDTO getPriceById(Long id) throws TariffNotFoundException {

        Optional<Price> optionalPrice = priceRepository.findById(id);

        return optionalPrice.map(price -> modelMapper.map(price, PriceDTO.class))

                .orElseThrow(() -> new TariffNotFoundException("Price not found for ID: " + id));
    }

    public List<PriceDTO> getAllPrices() {
        List<Price> prices = priceRepository.findAll();

        return prices.stream()
                .map(price -> modelMapper.map(price, PriceDTO.class))
                .toList();
    }

    public void deletePriceById(Long id) {
        priceRepository.deleteById(id);
    }

    @Retryable(maxAttempts = 3, retryFor = {TariffNotFoundException.class})
    @Cacheable("prices")
    public List<PriceDTO> findPricesByCriterion(LocalDateTime applicationDate, Long productId, String brandId) {
        log.info("Inside findPricesByCriterion function");
        try {
            Optional<Price> selectedPrice = priceRepository.findSelectedPrice(applicationDate, productId, brandId);

            if (selectedPrice.isEmpty()) {
                throw new TariffNotFoundException("Price not found");
            }

            PriceDTO selectedPriceDTO = modelMapper.map(selectedPrice.get(), PriceDTO.class);

            return Collections.singletonList(selectedPriceDTO);
        } catch (TariffNotFoundException e) {
            log.error("Error searching for Price: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
