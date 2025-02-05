package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "UserData")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_allergy_id", referencedColumnName = "id", nullable = true)
    private UserAllergy userAllergy;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "weight", nullable = false)
    private double weight;



    @Column(name = "abdomen", nullable = true)
    private int abdomen;

    @Column(name = "hips", nullable = true)
    private int hips;

    @Column(name = "waist", nullable = true)
    private int waist;

    @Column(name = "arm", nullable = true)
    private int arm;

    @Column(name = "chest", nullable = true)
    private int chest;
}
