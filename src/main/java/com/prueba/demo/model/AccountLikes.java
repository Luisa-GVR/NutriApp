package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountLikes")
public class AccountLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountDataID", referencedColumnName = "id")
    private AccountData accountData;


    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }
}
