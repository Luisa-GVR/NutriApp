package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountAllergy")
public class AccountAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountDataID", referencedColumnName = "id")
    private AccountData accountdata;



    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public AccountData getAccountdata() {
        return accountdata;
    }

    public void setAccountdata(AccountData accountdata) {
        this.accountdata = accountdata;
    }
}
