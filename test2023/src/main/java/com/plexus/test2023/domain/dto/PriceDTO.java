package com.plexus.test2023.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceDTO implements Serializable {

    private Long id;

    private String brandId;

    private String brandName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long tariffId;

    private Long productId;

    private Integer priority;

    private BigDecimal price;

    private String currency;
}
