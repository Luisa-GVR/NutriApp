package com.prueba.demo.model;

import jakarta.persistence.Transient;

import java.util.List;

public class FoodsResponse {
    @Transient
    private List<Foods> foods;

    // Getter y setter
    public List<Foods> getFoods() {
        return foods;
    }
    //Getters y setters...

    public void setFoods(List<Foods> foods) {
        this.foods = foods;
    }
}