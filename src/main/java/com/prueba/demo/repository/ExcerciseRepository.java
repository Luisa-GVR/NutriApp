package com.prueba.demo.repository;

import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExcerciseRepository extends JpaRepository<Excercise, Long> {


    Optional<Excercise> findByExcerciseName(String excerciseName);

    Excercise findFirstByExcerciseName(String name);


}
