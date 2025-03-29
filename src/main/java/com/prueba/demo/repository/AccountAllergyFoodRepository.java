package com.prueba.demo.repository;

import com.prueba.demo.model.AccountAllergyFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountAllergyFoodRepository extends JpaRepository<AccountAllergyFood, Long> {

    List<AccountAllergyFood> findAllByAccountAllergyId(Long accountAllergyId);


}
