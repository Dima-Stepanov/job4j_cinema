package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;

import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * UserDBStoreTest. test liquibase.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
public class UserDBStoreTest {
    static BasicDataSource pool;

    @BeforeClass
    public static void initPool() {
        pool = new Main().loadPool();
    }

    @AfterClass
    public static void closePool() throws SQLException {
        pool.close();
    }

    @After
    public void wipeTableUsers() throws SQLException {
        try (Statement statement = pool.getConnection().createStatement()) {
            statement.execute("DELETE FROM users;"
                    + "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        }
    }

    @Test
    public void whenCreateUser() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        User userInDb = store.findById(user.getId()).get();
        assertThat(user.getName(), is(userInDb.getName()));
    }

    @Test
    public void whenUpdate() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findAll() {
    }
}