package com.prueba.demo.repository;

import com.prueba.demo.model.Account;
import com.prueba.demo.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.name, ad.age, ad.gender, ad.height, ad.weight, ad.chest, ad.abdomen, ad.hips, ad.waist, ad.arm, ad.neck, r, " +
            "dm.breakfast, dm.lunch, dm.dinner, dm.snack, dm.optional, " +
            "e.excerciseName " +
            "FROM Account a " +
            "JOIN a.accountData ad " +
            "LEFT JOIN Report r ON r.accountData = ad " +
            "LEFT JOIN r.dayMeal dm " +
            "LEFT JOIN r.dayExcercise de " +
            "LEFT JOIN de.excercises e " +
            "WHERE a.id = :accountId " +
            "AND r.date >= :startDate AND r.date <= :endDate")
    List<Object[]> findAccountDetailsAndReports(@Param("accountId") Long accountId,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);


    @Query("SELECT r FROM Report r " +
            "WHERE r.accountData.account.id = :accountId " +
            "AND r.date >= :startDate AND r.date <= :endDate")
    List<Report> findReportsByAccountAndDateRange(@Param("accountId") Long accountId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);



}
