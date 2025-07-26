package com.project.logitrack.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LogisticCenter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticCenter {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		private String city;
		private String state;
		
		private String address;
		
		private String contactPhone;
		
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
