package com.project.logitrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tracking_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Status is required")
    private String status;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    private OffsetDateTime timestamp;

    @Column(name = "createdat")
    private OffsetDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    @NotNull(message = "Shipment is required")
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private LogisticCenter center;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedByUser;


    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.timestamp = OffsetDateTime.now();
    }


	public String getLocation() {
		if (this.center != null) {
	        return this.center.getName() + ", " + this.center.getCity();
	    }
	    return "Location information not available";
	}
}
