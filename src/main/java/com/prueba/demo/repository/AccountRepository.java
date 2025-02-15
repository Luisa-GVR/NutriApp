package com.prueba.demo.repository;

import com.prueba.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.name, ad.age, ad.gender, ad.height, ad.weight, ad.chest, ad.abdomen, ad.hips, ad.waist, ad.arm, ad.neck, r, " +
            "dm.breakfast, dm.lunch, dm.dinner, dm.snack, dm.optional " +
            "FROM Account a " +
            "JOIN a.accountData ad " +
            "LEFT JOIN Report r ON r.accountData = ad " +
            "LEFT JOIN r.dayMeal dm " +
            "LEFT JOIN r.dayExcercise de " +
            "WHERE a.id = :accountId AND r.date BETWEEN :startDate AND :endDate")
    List<Object[]> findAccountDetailsAndReportsInDateRange(@Param("accountId") Long accountId,
                                                           @Param("startDate") java.sql.Date startDate,
                                                           @Param("endDate") java.sql.Date endDate);



}
