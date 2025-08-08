package com.project.logitrack.Entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Logisticcenter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name for Logistic Center is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "City for logistic Center is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    private String city;

    @NotBlank(message = "State for logistic Center is required")
    @Size(max = 100, message = "State must be less than 100 characters")
    private String state;

    @NotBlank(message = "Address for logistic Center is required")
    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;
    
    @NotBlank(message = "Postal code is required")
    @Size(min = 6, max = 6, message = "Postal code must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "Postal code must contain only digits")
    private String postalcode;
    
    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Invalid contact phone format")
    private String contactPhone;

    @Column(name = "createdat", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updatedat")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
