package com.project.logitrack.Entity;

import jakarta.persistence.*;
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

    private String trackingId;

    private String status = "created";

    private BigDecimal weight;

    private String dimensions;

    private OffsetDateTime expectedDelivery;

    private OffsetDateTime actualDelivery;

    @Column(name = "createdat")
    private OffsetDateTime createdAt;

    @Column(name = "updatedat")
    private OffsetDateTime updatedAt;

    // == RELATIONSHIPS ==

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "source_center_id")
    private LogisticCenter sourceCenter;

    @ManyToOne
    @JoinColumn(name = "dest_center_id")
    private LogisticCenter destCenter;

    @ManyToOne
    @JoinColumn(name = "current_center_id")
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

