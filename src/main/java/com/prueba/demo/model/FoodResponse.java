package com.prueba.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Transient;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
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