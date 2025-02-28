package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DayMealFoods")
public class DayMealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "dayMealID", referencedColumnName = "id")
    private DayMeal dayMeal;

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public DayMeal getDayMeal() {
        return dayMeal;
    }

    public void setDayMeal(DayMeal dayMeal) {
        this.dayMeal = dayMeal;
    }
}
