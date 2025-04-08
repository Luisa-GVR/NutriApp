package com.prueba.demo.modelFreeSQL;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "AccountFreeSQL")
public class AccountFreeSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "accountFreeSQL", cascade = CascadeType.ALL)
    private AccountDataFreeSQL accountDataFreeSQL;

    @OneToMany(mappedBy = "accountFreeSQL", cascade = CascadeType.ALL)
    private List<AccountDataFreeSQLHistory> accountDataFreeSQLHistory;


    //Getters y setters...

    public List<AccountDataFreeSQLHistory> getAccountDataFreeSQLHistory() {
        return accountDataFreeSQLHistory;
    }

    public void setAccountDataFreeSQLHistory(List<AccountDataFreeSQLHistory> accountDataFreeSQLHistory) {
        this.accountDataFreeSQLHistory = accountDataFreeSQLHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountDataFreeSQL getAccountDataFreeSQL() {
        return accountDataFreeSQL;
    }

    public void setAccountDataFreeSQL(AccountDataFreeSQL accountDataFreeSQL) {
        this.accountDataFreeSQL = accountDataFreeSQL;
    }
}
