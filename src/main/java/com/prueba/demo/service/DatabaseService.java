package com.prueba.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    private final JdbcTemplate awsJdbcTemplate;

    // Inyectar ambos JdbcTemplate (AWS y H2)
    @Autowired
    public DatabaseService(
            @Qualifier("awsJdbcTemplate") JdbcTemplate awsJdbcTemplate){
        this.awsJdbcTemplate = awsJdbcTemplate;
    }

    // Método para obtener el nombre de la base de datos en AWS
    public String getAWSDatabaseName() {
        String sql = "SELECT DATABASE()";
        return awsJdbcTemplate.queryForObject(sql, String.class);
    }


    // Método para obtener la hora de la base de datos en AWS
    public String getAWSDatabaseTime() {
        String sql = "SELECT NOW()";
        return awsJdbcTemplate.queryForObject(sql, String.class);
    }

    public List<String> getAllTables() {
        String sql = "SHOW TABLES";
        return awsJdbcTemplate.queryForList(sql, String.class);  // Ejecutar la consulta y devolver una lista de nombres de tablas
    }
}


    /*

    AccountAWSRepository accountAWSRepository;
    AccountDataAWSRepository accountDataAWSRepository;

    @Autowired
    public DatabaseService(
            @Qualifier("accountAWS") AccountAWSRepository accountAWSRepository,
            @Qualifier("accountDataAWS") AccountDataAWSRepository accountDataAWSRepository{
        this.accountAWSRepository = accountAWSRepository;
        this.accountDataAWSRepository = accountDataAWSRepository;
    }



     */


