package ru.job4j.cinema;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
@SpringBootApplication
public class CinemaApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(CinemaApplication.class.getName());

    private Properties loadDbProperties() {
        LOGGER.info("Загрузка настроек приложения");
        Properties config = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        CinemaApplication.class.getClassLoader().getResourceAsStream(
                                "db.properties")))) {
            config.load(io);
        } catch (IOException e) {
            LOGGER.error("Не удалось загрузить настройки { }", e.getCause());
        }
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            LOGGER.error("Не удалось загрузить настройки { }", e.getCause());
        }
        return config;
    }

    @Bean
    public BasicDataSource loadPool() {
        Properties config = loadDbProperties();
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName(config.getProperty("jdbc.driver"));
        pool.setUrl(config.getProperty("jdbc.url"));
        pool.setUsername(config.getProperty("jdbc.username"));
        pool.setPassword(config.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(8);
        pool.setMaxOpenPreparedStatements(100);
        return pool;
    }

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
        System.out.println("Go to http://localhost:8080/");
    }
}
