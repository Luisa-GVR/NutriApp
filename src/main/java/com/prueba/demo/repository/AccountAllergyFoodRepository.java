package com.prueba.demo.repository;

import com.prueba.demo.model.AccountAllergyFood;
import com.prueba.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountAllergyFoodRepository extends JpaRepository<AccountAllergyFood, Long> {

    @Query("SELECT f.foodName " +
            "FROM AccountAllergyFood aaf " +
            "JOIN aaf.food f " +
            "WHERE aaf.accountAllergy.accountdata.id = :accountDataId")
    List<String> findFoodNamesByAccountDataId(@Param("accountDataId") Long accountDataId);


}
