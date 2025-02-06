package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DayMealsFoods")
public class DayMealsFoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Foods foods;

    @ManyToOne
    @JoinColumn(name = "dayMealsID", referencedColumnName = "id")
    private DayMeals dayMeals;

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Foods getFoods() {
        return foods;
    }

    public void setFoods(Foods foods) {
        this.foods = foods;
    }

    public DayMeals getDayMeals() {
        return dayMeals;
    }

    public void setDayMeals(DayMeals dayMeals) {
        this.dayMeals = dayMeals;
    }
}
