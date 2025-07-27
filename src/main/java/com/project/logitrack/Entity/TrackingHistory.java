package com.project.logitrack.Entity;

import jakarta.persistence.*;
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

    private String status;

    private String statusCode;

    private String notes;

    private OffsetDateTime timestamp;

    @Column(name = "createdat")
    private OffsetDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
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
		return "Didn't know which location we need to provide";
	}
}
