package com.prueba.demo.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userDataID", referencedColumnName = "id")
    private UserData userData;

    @ManyToOne
    @JoinColumn(name = "dayMealsID", referencedColumnName = "id")
    private DayMeal dayMeal;

    @ManyToOne
    @JoinColumn(name = "dayExcerciseID", referencedColumnName = "id")
    private DayExcercise dayExcercise;

    @Column
    private boolean goalMet;

    @Column
    private Date date;


    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public DayMeal getDayMeals() {
        return dayMeal;
    }

    public void setDayMeals(DayMeal dayMeal) {
        this.dayMeal = dayMeal;
    }

    public DayExcercise getDayExcercise() {
        return dayExcercise;
    }

    public void setDayExcercise(DayExcercise dayExcercise) {
        this.dayExcercise = dayExcercise;
    }

    public boolean isGoalMet() {
        return goalMet;
    }

    public void setGoalMet(boolean goalMet) {
        this.goalMet = goalMet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
