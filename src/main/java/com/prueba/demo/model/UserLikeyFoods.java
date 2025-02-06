package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserLikeyFoods")
public class UserLikeyFoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Foods foods;

    @ManyToOne
    @JoinColumn(name = "userLikey", referencedColumnName = "id")
    private UserLikey userLikey;

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

    public UserLikey getUserLikey() {
        return userLikey;
    }

    public void setUserLikey(UserLikey userLikey) {
        this.userLikey = userLikey;
    }
}
