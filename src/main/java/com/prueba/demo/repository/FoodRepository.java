package com.prueba.demo.repository;

import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Food findFirstByFoodName(String name);

    @Query("SELECT p.thumb FROM Food f JOIN f.photo p WHERE f.foodName = :foodName")
    Optional<String> findThumbnailURLByFoodName(@Param("foodName") String foodName);

    Optional<Food> findByFoodName(String foodName);


    @Query("SELECT f FROM Food f WHERE f.mealType IN :mealType AND f.calories BETWEEN :minCalories AND :maxCalories")
    List<Food> findFoodsByMealTypeAndCaloriesRange(@Param("mealType") List<Integer> mealType,
                                                   @Param("minCalories") double minCalories,
                                                   @Param("maxCalories") double maxCalories,
                                                   Pageable pageable);


    @Query("""
    SELECT f.foodName 
    FROM Food f 
    WHERE f.mealType IN :mealType 
    AND f.calories BETWEEN :minCalories AND :maxCalories
    AND f.foodName NOT IN (
        SELECT f.foodName FROM AccountDislikesFood aaf JOIN aaf.food f WHERE aaf.accountDislikes.accountData.id = :accountDataId
    )
    AND f.foodName NOT IN (
        SELECT f.foodName FROM AccountAllergyFood aaf JOIN aaf.food f WHERE aaf.accountAllergy.accountdata.id = :accountDataId
    )
""")
    List<String> findValidFoods(@Param("mealType") List<Integer> mealType,
                                @Param("minCalories") double minCalories,
                                @Param("maxCalories") double maxCalories,
                                @Param("accountDataId") Long accountDataId,
                                Pageable pageable);


}
