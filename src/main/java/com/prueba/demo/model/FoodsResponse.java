package com.prueba.demo.model;

import java.util.List;

public class FoodsResponse {
    private List<Food> foods;

    // Getter y setter
    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}