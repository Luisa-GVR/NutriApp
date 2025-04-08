package com.prueba.demo.modelFreeSQL;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "AccountDataFreeSQLHistory")
public class AccountDataFreeSQLHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountFreeSQL_id")
    private AccountFreeSQL accountFreeSQL;


    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "abdomen", nullable = true)
    private Double abdomen;

    @Column(name = "hips", nullable = true)
    private Double hips;

    @Column(name = "waist", nullable = true)
    private Double waist;

    @Column(name = "arm", nullable = true)
    private Double arm;

    @Column(name = "chest", nullable = true)
    private Double chest;

    @Column(name = "neck", nullable = true)
    private Double neck;

    @Column(name = "date")
    private Date date;

    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountFreeSQL getAccountFreeSQL() {
        return accountFreeSQL;
    }

    public void setAccountFreeSQL(AccountFreeSQL accountFreeSQL) {
        this.accountFreeSQL = accountFreeSQL;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(Double abdomen) {
        this.abdomen = abdomen;
    }

    public Double getHips() {
        return hips;
    }

    public void setHips(Double hips) {
        this.hips = hips;
    }

    public Double getWaist() {
        return waist;
    }

    public void setWaist(Double waist) {
        this.waist = waist;
    }

    public Double getArm() {
        return arm;
    }

    public void setArm(Double arm) {
        this.arm = arm;
    }

    public Double getChest() {
        return chest;
    }

    public void setChest(Double chest) {
        this.chest = chest;
    }

    public Double getNeck() {
        return neck;
    }

    public void setNeck(Double neck) {
        this.neck = neck;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
