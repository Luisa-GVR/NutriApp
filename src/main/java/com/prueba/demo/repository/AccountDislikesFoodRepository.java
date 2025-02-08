package com.prueba.demo.repository;

import com.prueba.demo.model.AccountAllergyFood;
import com.prueba.demo.model.AccountDislikes;
import com.prueba.demo.model.AccountDislikesFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDislikesFoodRepository extends JpaRepository<AccountDislikesFood, Long> {

    @Query("SELECT f.foodName " +
            "FROM AccountDislikesFood aaf " +
            "JOIN aaf.food f " +
            "WHERE aaf.accountDislikes.accountData.id = :accountDataId")
    List<String> findFoodNamesByAccountDataId(@Param("accountDataId") Long accountDataId);


    List<AccountDislikesFood> findByAccountDislikes(AccountDislikes accountDislikes);


}
