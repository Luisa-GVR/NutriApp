package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountData")
public class AccountData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "accountAllergyId", referencedColumnName = "id")
    private AccountAllergy accountAllergy;

    @Enumerated(EnumType.STRING)
    @Column
    private Goal goal;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "gender", nullable = false)
    private Boolean gender;


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




    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AccountAllergy getAccountAllergy() {
        return accountAllergy;
    }

    public void setAccountAllergy(AccountAllergy accountAllergy) {
        this.accountAllergy = accountAllergy;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
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
}
