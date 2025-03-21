package com.prueba.demo.modelAWS;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountAWS")
public class AccountAWS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "accountAWS", cascade = CascadeType.ALL)
    private AccountDataAWS accountDataAWS;
    //Getters y setters...

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

    public AccountDataAWS getAccountDataAWS() {
        return accountDataAWS;
    }

    public void setAccountDataAWS(AccountDataAWS accountDataAWS) {
        this.accountDataAWS = accountDataAWS;
    }
}
