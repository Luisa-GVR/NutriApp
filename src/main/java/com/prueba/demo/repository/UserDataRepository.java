package com.prueba.demo.repository;

import com.prueba.demo.model.User;
import com.prueba.demo.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
}
