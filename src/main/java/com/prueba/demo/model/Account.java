package com.prueba.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountData accountData;
    //Getters y setters...

    public void setId(Long id) {
        this.id = id;
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }


}
