package com.prueba.demo.repository;

import com.prueba.demo.model.DayExcercise;
import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayExcerciseRepository extends JpaRepository<DayExcercise, Long> {
    List<DayExcercise> findAllByDate(Date valueOf);

    List<DayExcercise> findByDate(Date date);

}
