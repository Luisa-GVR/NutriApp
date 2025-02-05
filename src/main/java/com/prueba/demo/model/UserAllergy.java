package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserAllergy")
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




}
