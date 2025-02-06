package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DayMeals")
public class DayMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Breakfast",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> breakfast;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Lunch",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> lunch;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Dinner",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> dinner;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Snack",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> snack;

    @Column(name = "date", nullable = false, length = 120)
    private Date date;


    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Food> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Food> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Food> getLunch() {
        return lunch;
    }

    public void setLunch(List<Food> lunch) {
        this.lunch = lunch;
    }

    public List<Food> getDinner() {
        return dinner;
    }

    public void setDinner(List<Food> dinner) {
        this.dinner = dinner;
    }

    public List<Food> getSnack() {
        return snack;
    }

    public void setSnack(List<Food> snack) {
        this.snack = snack;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
