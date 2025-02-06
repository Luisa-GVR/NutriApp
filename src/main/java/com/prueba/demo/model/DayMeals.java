package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DayMeals")
public class DayMeals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Breakfast",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Foods> breakfast;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Lunch",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Foods> lunch;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Dinner",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Foods> dinner;

    @ManyToMany
    @JoinTable(
            name = "DayMeals_Snack",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Foods> snack;

    @Column(name = "date", nullable = false, length = 120)
    private Date date;


    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Foods> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Foods> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Foods> getLunch() {
        return lunch;
    }

    public void setLunch(List<Foods> lunch) {
        this.lunch = lunch;
    }

    public List<Foods> getDinner() {
        return dinner;
    }

    public void setDinner(List<Foods> dinner) {
        this.dinner = dinner;
    }

    public List<Foods> getSnack() {
        return snack;
    }

    public void setSnack(List<Foods> snack) {
        this.snack = snack;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
