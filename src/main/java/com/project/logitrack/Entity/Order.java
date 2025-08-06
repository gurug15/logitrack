package com.project.logitrack.Entity;

import jakarta.persistence.*;
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
    private String customername;

    @Column(nullable = false)
    private String customerphone;
    
    @Column(nullable = false)
    private String customeremail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String customeraddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalcode;

    @Column(nullable = false)
    private String country = "India";

    @Column(nullable = false)
    private LocalDate orderdate = LocalDate.now();

    @Column(nullable = false)
    private String status = "pending";

    @Column(nullable = false)
    private BigDecimal totalprice;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.DETACH, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
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

