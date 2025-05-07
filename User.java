package com.incident.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phoneNumber;
    
    private String address;
    
    private String pinCode;
    
    private String city;
    
    private String country;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    private List<Incident> incidents = new ArrayList<>();
}