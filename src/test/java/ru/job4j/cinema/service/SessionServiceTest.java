package ru.job4j.cinema.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import ru.job4j.cinema.CinemaApplication;


import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 19.04.2022
 */
public class SessionServiceTest {
    static BasicDataSource pool;

    @BeforeClass
    public static void initPool() {
        pool = new CinemaApplication().loadPool();
    }

    @AfterClass
    public static void closePool() throws SQLException {
        pool.close();
    }

    @After
    public void wipeTableUsers() throws SQLException {
        try (Statement statement = pool.getConnection().createStatement()) {
            statement.execute("DELETE FROM sessions;"
                    + "ALTER TABLE sessions ALTER COLUMN session_id RESTART WITH 1");
        }
    }
}