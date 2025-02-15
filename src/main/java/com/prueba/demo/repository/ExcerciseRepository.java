package com.prueba.demo.repository;

import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcerciseRepository extends JpaRepository<Excercise, Long> {
}
