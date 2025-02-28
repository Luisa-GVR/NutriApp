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
    @JoinColumn(name = "accountAllergyId", referencedColumnName = "id", nullable = true)
    private AccountAllergy accountAllergy;

    @Enumerated(EnumType.STRING)
    @Column
    private Goal goal;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType monday;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType tuesday;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType wednesday;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType thursday;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType friday;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "age", nullable = false)
    private int age;

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

    @Column(name = "neck", nullable = true)
    private Double neck;


    //Getters y setters...


    public ExcerciseType getMonday() {
        return monday;
    }

    public void setMonday(ExcerciseType monday) {
        this.monday = monday;
    }

    public ExcerciseType getTuesday() {
        return tuesday;
    }

    public void setTuesday(ExcerciseType tuesday) {
        this.tuesday = tuesday;
    }

    public ExcerciseType getWednesday() {
        return wednesday;
    }

    public void setWednesday(ExcerciseType wednesday) {
        this.wednesday = wednesday;
    }

    public ExcerciseType getThursday() {
        return thursday;
    }

    public void setThursday(ExcerciseType thursday) {
        this.thursday = thursday;
    }

    public ExcerciseType getFriday() {
        return friday;
    }

    public void setFriday(ExcerciseType friday) {
        this.friday = friday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getNeck() {
        return neck;
    }

    public void setNeck(Double neck) {
        this.neck = neck;
    }

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
