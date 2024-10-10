package com.firm.brokage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String assetName;
    private String orderSide; // BUY or SELL
    private int size;
    private double price;
    private String status; // PENDING, MATCHED, CANCELED
    private LocalDateTime createDate;
}
