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

    //Getters y setters...


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
