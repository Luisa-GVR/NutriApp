package com.prueba.demo.repository;

import com.prueba.demo.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    Report findByDate(Date reportDate);
}
