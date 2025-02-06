package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountAllergyFoods")
public class AccountAllergyFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FoodsID", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "accountAllergy", referencedColumnName = "id")
    private AccountAllergy accountAllergy;

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

    public AccountAllergy getAccountAllergy() {
        return accountAllergy;
    }

    public void setAccountAllergy(AccountAllergy accountAllergy) {
        this.accountAllergy = accountAllergy;
    }
}
