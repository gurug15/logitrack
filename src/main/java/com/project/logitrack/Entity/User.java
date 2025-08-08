package com.project.logitrack.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    
    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Column(name = "passwordhash") //there is no validation needed
    private String passwordHash;
    
    @Column(name = "phone")
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Invalid contact phone format")
    private String phone;
    
    @ManyToOne
    @JoinColumn(name = "roleid") 
    @JsonIgnoreProperties("users")
    @NotNull(message = "Role is required")
    private Roles roleId;
    
    @ManyToOne
    @JoinColumn(name = "Logisticcenterid")
    private LogisticCenter logisticCenterId;
    
 	@Column(name = "createdat")
    private OffsetDateTime createdAt;
    
    @Column(name = "updatedat")
    private OffsetDateTime updatedAt;

    
    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        if (this.roleId == null) {
            Roles defaultRole = new Roles();
            defaultRole.setId(3); // Default to vendor role
            this.roleId = defaultRole;
        }
        
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
    
}
