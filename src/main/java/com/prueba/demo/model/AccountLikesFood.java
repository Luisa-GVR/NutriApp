package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountLikesFood")
public class AccountLikesFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "accountLikes", referencedColumnName = "id")
    private AccountLikes accountLikes;

    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public AccountLikes getAccountLikes() {
        return accountLikes;
    }

    public void setAccountLikes(AccountLikes accountLikes) {
        this.accountLikes = accountLikes;
    }
}
