package com.project.logitrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders") 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to User (userId)
    @ManyToOne
    @JoinColumn(
        name = "userid",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_order_user")
    )
    private User user;

    // Embedded customer info
    @Column(nullable = false)
    @NotBlank(message = "Customer name is required")
    @Size(min = 3, max = 100, message = "Customer name must be between 3 and 100 characters")
    private String customername;

    @Column(nullable = false)
    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Invalid contact phone format")
    private String customerphone;
    
    @Column(nullable = false)
    @NotBlank(message = "Customer email is required")
    @Email(message = "Please enter a valid email address")
    private String customeremail;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Customer address is required")
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String customeraddress;

    @Column(nullable = false)
    @NotBlank(message = "City is required")
    private String city;

    @Column(nullable = false)
    @NotBlank(message = "State is required")
    private String state;

    @Column(nullable = false)
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{6}", message = "Postal code must be exactly 6 digits")
    private String postalcode;

    @Column(nullable = false)
    @NotBlank(message = "Country is required")
    private String country = "India";

    @Column(nullable = false)
    private LocalDate orderdate = LocalDate.now();

    @Column(nullable = false)
    private String status = "pending";

    @Column(nullable = false)
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be positive")
    private BigDecimal totalprice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Size(min = 1, message = "Order must contain at least one item")
    private List<OrderItem> orderItems;
    	
    @Column(name = "expecteddeliverydate")
    private LocalDate expectedDeliveryDate;  //this is required to show the expected delivery date

    @Column(name = "delivereddate")
    private LocalDate deliveredDate; //this is required to show the expected delivery date
    
    
    @Column(name = "createdat", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedat", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();

}

