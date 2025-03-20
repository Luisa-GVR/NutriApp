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
        basePackages = "com.prueba.demo.repositoryAWS",
        entityManagerFactoryRef = "awsEntityManagerFactory",
        transactionManagerRef = "awsTransactionManager"
)
public class AWSDatabaseConfig {

    @Value("${aws.datasource.url}")
    private String url;

    @Value("${aws.datasource.username}")
    private String username;

    @Value("${aws.datasource.password}")
    private String password;


    @Bean(name = "awsDataSource")
    public DataSource awsDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean(name = "awsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean awsEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(awsDataSource())
                .packages("com.prueba.demo.modelAWS")
                .persistenceUnit("aws")
                .build();
    }

    @Bean(name = "awsJdbcTemplate")
    public JdbcTemplate awsJdbcTemplate(@Qualifier("awsDataSource") DataSource awsDataSource) {
        return new JdbcTemplate(awsDataSource);
    }

    @Bean(name = "awsTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("awsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}
