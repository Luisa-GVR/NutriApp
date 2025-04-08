package com.prueba.demo.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.prueba.demo.repositoryFreeSQL",
        entityManagerFactoryRef = "freeSQLEntityManagerFactory",
        transactionManagerRef = "freeSQLTransactionManager"
)
public class FreeSQLDatabaseConfig {

    @Value("${freeSQL.datasource.url}")
    private String url;

    @Value("${freeSQL.datasource.username}")
    private String username;

    @Value("${freeSQL.datasource.password}")
    private String password;


    @Bean(name = "freeSQLDataSource")
    public DataSource freeSQLDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean(name = "freeSQLEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean freeSQLEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(freeSQLDataSource())
                .packages("com.prueba.demo.modelFreeSQL")
                .persistenceUnit("freeSQL")
                .build();
    }

    @Bean(name = "freeSQLJdbcTemplate")
    public JdbcTemplate freeSQLJdbcTemplate(@Qualifier("freeSQLDataSource") DataSource freeSQLDataSource) {
        return new JdbcTemplate(freeSQLDataSource);
    }

    @Bean(name = "freeSQLTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("freeSQLEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}
