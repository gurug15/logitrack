package com.project.logitrack.Entity;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
		@NotBlank(message = "Category name is required")
		@Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
	    private String name;

		@Column(name = "description")
		@Size(max = 255, message = "Category description should be less than or equal to 255")
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
