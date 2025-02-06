package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserAllergyFoods")
public class UserAllergyFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Food food;

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

    public Food getFoods() {
        return food;
    }

    public void setFoods(Food food) {
        this.food = food;
    }

    public UserAllergy getUserAllergy() {
        return userAllergy;
    }

    public void setUserAllergy(UserAllergy userAllergy) {
        this.userAllergy = userAllergy;
    }
}
