package com.project.logitrack.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

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
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "passwordhash")
    private String passwordHash;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "roleid")
    private Integer roleId;
    
    @Column(name = "logisticcenterid")
    private Integer logisticCenterId;
    
    @Column(name = "createdat")
    private LocalDateTime createdAt;
    
    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
    
    // Custom constructor for basic fields
    public User(String name, String email, String passwordHash, Integer roleId) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
