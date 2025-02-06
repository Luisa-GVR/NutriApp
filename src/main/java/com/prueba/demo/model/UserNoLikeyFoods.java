package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserNoLikeyFoods")
public class UserNoLikeyFoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Foods foods;

    @ManyToOne
    @JoinColumn(name = "userNoLikey", referencedColumnName = "id")
    private UserNoLikey userNoLikey;



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

    public UserNoLikey getUserNoLikey() {
        return userNoLikey;
    }

    public void setUserNoLikey(UserNoLikey userNoLikey) {
        this.userNoLikey = userNoLikey;
    }
}
