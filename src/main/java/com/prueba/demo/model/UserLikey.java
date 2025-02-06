package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "UserLikey")
public class UserLikey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userDataID", referencedColumnName = "id")
    private UserData userData;


    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getUser() {
        return userData;
    }

    public void setUser(UserData user) {
        this.userData = user;
    }


}
