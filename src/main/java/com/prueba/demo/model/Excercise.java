package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Excercise")
public class Excercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "excercisename", nullable = false, length = 120)
    private String excercisename;

    @Column(name = "met", nullable = false)
    private int met;

    @Column(name = "excercisegoal", nullable = false)
    private boolean excercisegoal;

    public Long getId() {
        return id;
    }

    public String getExcercisename() {
        return excercisename;
    }

    public void setExcercisename(String excercisename) {
        this.excercisename = excercisename;
    }

    public int getMet() {
        return met;
    }

    public void setMet(int met) {
        this.met = met;
    }

    public boolean isExcercisegoal() {
        return excercisegoal;
    }

    public void setExcercisegoal(boolean excercisegoal) {
        this.excercisegoal = excercisegoal;
    }



}
