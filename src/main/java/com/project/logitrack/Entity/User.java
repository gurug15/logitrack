package com.project.logitrack.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

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
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "passwordhash")
    private String passwordHash;
    
    @Column(name = "phone")
    private String phone;
    
    @ManyToOne
    @JoinColumn(name = "roleid") 
    @JsonIgnoreProperties("users")
    private Roles roleId;
    
    @Column(name = "logisticcenterid")
    private Integer logisticCenterId;
    
    @Column(name = "createdat")
    private LocalDateTime createdAt;
    
    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
    
}
