package com.plexus.test2023.infrastructure.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name="start_date")
    private LocalDateTime startDate;

    @Column(name="end_date")
    private LocalDateTime endDate;

    @Column(name="tariff_id")
    private Long tariffId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="priority")
    private Integer priority;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="currency")
    private String currency;
}
