package com.prueba.demo.repository;

import com.prueba.demo.model.AccountLikesFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountLikesFoodRepository extends JpaRepository<AccountLikesFood, Long> {

    @Query("SELECT f.foodName " +
            "FROM AccountLikesFood alf " +
            "JOIN alf.food f " +
            "WHERE alf.accountLikes.accountData.id = :accountDataId")
    List<String> findFoodNamesByAccountDataId(@Param("accountDataId") Long accountDataId);

}
