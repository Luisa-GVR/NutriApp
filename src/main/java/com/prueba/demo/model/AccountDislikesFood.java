package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountDislikesFood")
public class AccountDislikesFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "accountDislikes", referencedColumnName = "id")
    private AccountDislikes accountDislikes;



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

    public AccountDislikes getAccountDislikes() {
        return accountDislikes;
    }

    public void setAccountDislikes(AccountDislikes accountDislikes) {
        this.accountDislikes = accountDislikes;
    }
}
