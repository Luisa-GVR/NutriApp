package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DayExcercise")
public class DayExcercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "time", nullable = false, length = 120)
    private int time;

    @Column(name = "date", nullable = false, length = 120)
    private Date date;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DayExcercise_Excercise",
            joinColumns = @JoinColumn(name = "dayExcercise_id"),
            inverseJoinColumns = @JoinColumn(name = "excercise_id")
    )
    private List<Excercise> excercises;


    //Getters y setters...


    public List<Excercise> getExcercises() {
        return excercises;
    }

    public void setExcercises(List<Excercise> excercises) {
        this.excercises = excercises;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    private void calculateTime() {
        if (excercises != null) {
            this.time = excercises.stream()
                    .mapToInt(Excercise::getTime)
                    .sum();
        } else {
            this.time = 0;
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
