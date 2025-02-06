package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserLikeyFoods")
public class UserLikesFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "userLikey", referencedColumnName = "id")
    private UserLikes userLikes;

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

    public UserLikes getUserLikey() {
        return userLikes;
    }

    public void setUserLikey(UserLikes userLikes) {
        this.userLikes = userLikes;
    }
}
