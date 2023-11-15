package com.plexus.test2023.infrastructure.repositories;

import com.plexus.test2023.infrastructure.models.Price;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Hidden
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("SELECT p FROM Price p " +
            "WHERE :applicationDate BETWEEN p.startDate AND p.endDate " +
            "AND p.productId = :productId " +
            "AND p.brand.brandId = :brandId " +
            "ORDER BY p.priority DESC " +
            "LIMIT 1")
    Optional<Price> findSelectedPrice(@Param("applicationDate") LocalDateTime applicationDate,
                                      @Param("productId") Long productId,
                                      @Param("brandId") String brandId);
}