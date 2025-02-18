package com.prueba.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "Excercise")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Excercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "excerciseName", nullable = false, length = 120)
    private String excerciseName;

    @Enumerated(EnumType.STRING)
    @Column
    private ExcerciseType excerciseType;

    @Column(name = "gifURL", nullable = false)
    private String gifURL;



    @Column(name = "time", nullable = false, length = 120)
    private int time;

    @Column(name = "reps", nullable = false, length = 120)
    private int reps;

    @Column(name = "series", nullable = false, length = 120)
    private int series;

    @Column(name = "calories", nullable = false, length = 120)
    private int calories;


    //Getters y setters...


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcerciseName() {
        return excerciseName;
    }

    public void setExcerciseName(String excerciseName) {
        this.excerciseName = excerciseName;
    }

    public ExcerciseType getExcerciseType() {
        return excerciseType;
    }

    public void setExcerciseType(ExcerciseType excerciseType) {
        this.excerciseType = excerciseType;
    }

    public String getGifURL() {
        return gifURL;
    }

    public void setGifURL(String gifURL) {
        this.gifURL = gifURL;
    }
}
