package com.prueba.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    private final JdbcTemplate freeSQLJdbcTemplate;

    // Inyectar ambos JdbcTemplate (FreeSQL y H2)
    @Autowired
    public DatabaseService(
            @Qualifier("freeSQLJdbcTemplate") JdbcTemplate freeSQLJdbcTemplate){
        this.freeSQLJdbcTemplate = freeSQLJdbcTemplate;
    }

    // Método para obtener el nombre de la base de datos en FreeSQL
    public String getFreeSQLDatabaseName() {
        String sql = "SELECT DATABASE()";
        return freeSQLJdbcTemplate.queryForObject(sql, String.class);
    }


    // Método para obtener la hora de la base de datos en FreeSQL
    public String getFreeSQLDatabaseTime() {
        String sql = "SELECT NOW()";
        return freeSQLJdbcTemplate.queryForObject(sql, String.class);
    }

    public List<String> getAllTables() {
        String sql = "SHOW TABLES";
        return freeSQLJdbcTemplate.queryForList(sql, String.class);  // Ejecutar la consulta y devolver una lista de nombres de tablas
    }
}





