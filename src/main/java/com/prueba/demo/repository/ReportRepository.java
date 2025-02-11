package com.prueba.demo.repository;

import com.prueba.demo.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.accountData.account.id = :accountId " +
            "AND r.date BETWEEN :monday AND :friday")
    List<Report> findReportsForWeek(@Param("accountId") Long accountId,
                                    @Param("monday") Date monday,
                                    @Param("friday") Date friday);
    Report findByDate(Date reportDate);
}
