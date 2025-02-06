package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserNoLikeyFoods")
public class UserDislikesFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "userNoLikey", referencedColumnName = "id")
    private UserDislikes userDislikes;



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

    public UserDislikes getUserNoLikey() {
        return userDislikes;
    }

    public void setUserNoLikey(UserDislikes userDislikes) {
        this.userDislikes = userDislikes;
    }
}
