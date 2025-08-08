package com.project.logitrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "shipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank(message = "Tracking ID is required")
	    @Column(unique = true)
	    private String trackingId;

	    @NotBlank(message = "Status is required")
	    @Pattern(regexp = "pending|shipped|in-transit|delivered|cancelled", message = "Invalid shipment status")
	    private String status;

	    @NotNull(message = "Weight is required")
	    @Positive(message = "Weight must be positive")
	    @Digits(integer = 10, fraction = 2, message = "Weight must have up to 2 decimal places")
	    private BigDecimal weight;

	    @Size(max = 255, message = "Dimensions must be less than 255 characters")
	    private String dimensions;

	    @NotNull(message = "Expected delivery date is required")
	    @Future(message = "Expected delivery date must be in the future")
	    private OffsetDateTime expectedDelivery;

	    @FutureOrPresent(message = "Actual delivery date cannot be in the past")
	    private OffsetDateTime actualDelivery;

	    @Column(name = "createdat", updatable = false)
	    private OffsetDateTime createdAt;

	    @Column(name = "updatedat")
	    private OffsetDateTime updatedAt;

	    // == RELATIONSHIPS ==

	    @OneToOne(optional = false)
	    @JoinColumn(name = "order_id", nullable = false)
	    @NotNull
	    private Order order;

	    @ManyToOne
	    @JoinColumn(name = "source_center_id")
	    @NotNull(message = "Source center is required")
	    private LogisticCenter sourceCenter;

	    @ManyToOne
	    @JoinColumn(name = "dest_center_id")
	    @NotNull(message = "Destination center is required")
	    private LogisticCenter destCenter;

	    @ManyToOne
	    @JoinColumn(name = "current_center_id")
	    @NotNull(message = "Current center is required")
	    private LogisticCenter currentCenter;

	    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<TrackingHistory> trackingHistory;

    // == CALLBACKS ==

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

