package com.prueba.demo.repository;

import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Food findFirstByFoodName(String name);

    @Query("SELECT p.thumb FROM Food f JOIN f.photo p WHERE f.foodName = :foodName")
    Optional<String> findThumbnailURLByFoodName(@Param("foodName") String foodName);

    Optional<Food> findByFoodName(String foodName);
}
