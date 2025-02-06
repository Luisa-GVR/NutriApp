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

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
