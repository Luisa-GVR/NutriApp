package com.prueba.demo.repository;

import com.prueba.demo.model.AccountAllergyFood;
import com.prueba.demo.model.DayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface DayMealRepository  extends JpaRepository<DayMeal, Long> {
    DayMeal findByDate(Date valueOf);
    List<DayMeal> findAllByDate(Date valueOf);
}
