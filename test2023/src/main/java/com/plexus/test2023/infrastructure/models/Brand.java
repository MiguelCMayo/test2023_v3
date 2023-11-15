package com.plexus.test2023.infrastructure.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @Column(name="brand_id")
    private String brandId;

    @Column(name="brand_name")
    private String brandName;

    public Brand() {
    }
}
