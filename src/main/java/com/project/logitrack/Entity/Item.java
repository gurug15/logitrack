package com.project.logitrack.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

	    private String name;

	    private String sku;

	    private String description;

	    private BigDecimal weight;

	    private String dimensions;


	    // Relationships
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "category_id")
	    private Category category;

	    @OneToMany(mappedBy = "item", cascade = CascadeType.DETACH, orphanRemoval = true)
	    private List<OrderItem> orderItems;

	 	@Column(name = "createdat")
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
