package com.prueba.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "UserAllergy")
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userDataID", referencedColumnName = "id")
    private UserData userdata;



    //Getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public UserData getUserdata() {
        return userdata;
    }

    public void setUserdata(UserData userdata) {
        this.userdata = userdata;
    }


}
