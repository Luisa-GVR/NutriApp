package com.prueba.demo.repository;

import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface FoodRepository extends JpaRepository<Food, Long> {

    public Optional<Food> findByFoodName(String name);


}
