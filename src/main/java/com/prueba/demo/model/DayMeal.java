package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "DayMeal")
public class DayMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DayMeal_Breakfast",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> breakfast = new ArrayList<>(); // Aquí inicializamos la lista

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DayMeal_Lunch",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> lunch = new ArrayList<>(); // Inicializar la lista

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DayMeal_Dinner",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> dinner = new ArrayList<>(); // Inicializar la lista

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DayMeal_Snack",
            joinColumns = @JoinColumn(name = "day_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> snack = new ArrayList<>(); // Inicializar la lista

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



    @Override
    public String toString() {
        return "DayMeal{" +
                "date=" + date +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", snack=" + snack +
                '}';
    }

}
