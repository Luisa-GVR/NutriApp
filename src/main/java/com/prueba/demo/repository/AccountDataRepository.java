package com.prueba.demo.repository;

import com.prueba.demo.model.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataRepository extends JpaRepository<AccountData, Long> {
}
