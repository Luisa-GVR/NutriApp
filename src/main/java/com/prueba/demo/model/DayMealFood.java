package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DayMealsFoods")
public class DayMealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "dayMealsID", referencedColumnName = "id")
    private DayMeal dayMeal;

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFoods() {
        return food;
    }

    public void setFoods(Food food) {
        this.food = food;
    }

    public DayMeal getDayMeals() {
        return dayMeal;
    }

    public void setDayMeals(DayMeal dayMeal) {
        this.dayMeal = dayMeal;
    }
}
