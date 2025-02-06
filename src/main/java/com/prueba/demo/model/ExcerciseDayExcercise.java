/*package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ExcerciseDayExcercise")
public class ExcerciseDayExcercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "excerciseID", referencedColumnName = "id")
    private Excercise excercise;

    @ManyToOne
    @JoinColumn(name = "dayExcerciseID", referencedColumnName = "id")
    private DayExcercise dayExcerciseID;

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Excercise getExcercise() {
        return excercise;
    }

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }

    public DayExcercise getDayExcerciseID() {
        return dayExcerciseID;
    }

    public void setDayExcerciseID(DayExcercise dayExcerciseID) {
        this.dayExcerciseID = dayExcerciseID;
    }
}



 */