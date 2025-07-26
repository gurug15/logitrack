package com.project.logitrack.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
		
	 	@Column(name = "createdat")
	    private LocalDateTime createdAt;
	    
	    @Column(name = "updatedat")
	    private LocalDateTime updatedAt;
	
	    
	    @PrePersist
	    protected void onCreate() {
	        this.createdAt = LocalDateTime.now();
	        this.updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }
	    
	
}
