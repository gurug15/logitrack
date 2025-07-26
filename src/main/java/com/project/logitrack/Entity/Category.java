package com.project.logitrack.Entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
		
		@Column(name = "name")
	    private String name;

		@Column(name = "description")
	    private String description;
		
		@OneToMany(mappedBy = "category")
		private List<Item> items;
		
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
