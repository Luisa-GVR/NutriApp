package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserAllergyFoods")
public class UserAllergyFoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Foods foods;

    @ManyToOne
    @JoinColumn(name = "userAllergy", referencedColumnName = "id")
    private UserAllergy userAllergy;

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

    public UserAllergy getUserAllergy() {
        return userAllergy;
    }

    public void setUserAllergy(UserAllergy userAllergy) {
        this.userAllergy = userAllergy;
    }
}
