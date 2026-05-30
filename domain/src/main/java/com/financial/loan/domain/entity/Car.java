package com.financial.loan.domain.entity;


import com.financial.loan.domain.domainexception.validation.CarValidationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Car {
    private final UUID id;
    private final String brand;
    private final String model;
    private final LocalDateTime year;
    private final BigDecimal cost;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Car create(
            String brand,
            String model,
            LocalDateTime year,
            BigDecimal cost) {

        validateBrand(brand);
        validateModel(model);
        validateYear(year);
        validateCost(cost);

        LocalDateTime now = LocalDateTime.now();

        return new Car(
                UUID.randomUUID(),
                brand,
                model,
                year,
                cost,
                now,
                now
        );
    }

    public Car updateBrand(String brand) {
        validateBrand(brand);
        return new Car(
                this.id,
                brand,
                this.model,
                this.year,
                this.cost,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Car updateModel(String model) {
        validateModel(model);
        return new Car(
                this.id,
                this.brand,
                model,
                this.year,
                this.cost,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Car updateYear(LocalDateTime year) {
        validateYear(year);
        return new Car(
                this.id,
                this.brand,
                this.model,
                year,
                this.cost,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Car updateCost(BigDecimal cost) {
        validateCost(cost);
        return new Car(
                this.id,
                this.brand,
                this.model,
                this.year,
                cost,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    private static void validateBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new CarValidationException("brand", "brand cannot be null or blank");
        }
    }

    private static void validateModel(String model) {
        if (model == null || model.isBlank()) {
            throw new CarValidationException("model", "model cannot be null or blank");
        }
    }

    private static void validateYear(LocalDateTime year) {
        if (year == null) {
            throw new CarValidationException("year", "year cannot be null");
        }
        if (year.isAfter(LocalDateTime.now())) {
            throw new CarValidationException("year", "year cannot be in the future");
        }
    }

    private static void validateCost(BigDecimal cost) {
        if (cost == null) {
            throw new CarValidationException("cost", "cost cannot be null");
        }
        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CarValidationException("cost", "cost must be greater than zero");
        }
    }
}