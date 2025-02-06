package com.prueba.demo.model;

import jakarta.persistence.Transient;

import java.util.List;

public class ExcerciseResponse {
    @Transient
    private List<Excercise> excercises;

    // Getter y setter
    public List<Excercise> getExcercises() {
        return excercises;
    }

    //Getters y setters...


    public void setExcercises(List<Excercise> excercises) {
        this.excercises = excercises;
    }

}
