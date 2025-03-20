package com.prueba.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    /*

    Ejemplo de como se hara en el futuro para inyectar repos:)

    private final AWSUserRepository awsUserRepository;
    private final AWSFoodRepository awsFoodRepository;
    private final AWSReportRepository awsReportRepository;

    @Autowired
    public DatabaseService(
            @Qualifier("awsUserRepository") AWSUserRepository awsUserRepository,
            @Qualifier("awsFoodRepository") AWSFoodRepository awsFoodRepository,
            @Qualifier("awsReportRepository") AWSReportRepository awsReportRepository) {
        this.awsUserRepository = awsUserRepository;
        this.awsFoodRepository = awsFoodRepository;
        this.awsReportRepository = awsReportRepository;
    }


    Los repos se verian algo asi

    @Repository
    public interface YourAWSRepository extends JpaRepository<YourAWSModel, Long> {
        // Métodos
    }


     */

}
