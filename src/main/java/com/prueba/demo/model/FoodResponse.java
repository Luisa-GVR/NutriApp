package com.prueba.demo.model;

import jakarta.persistence.Transient;

import java.util.List;

public class FoodResponse {
    @Transient
    private List<Food> foods;

    // Getter y setter
    public List<Food> getFoods() {
        return foods;
    }
    //Getters y setters...

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}