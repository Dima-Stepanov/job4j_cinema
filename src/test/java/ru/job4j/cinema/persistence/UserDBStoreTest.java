package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cinema.CinemaApplication;
import ru.job4j.cinema.model.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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
        pool = new CinemaApplication().loadPool();
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
    public void whenUpdateThenOk() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        user.setName("Ivan");
        User userInDb = store.update(user).get();
        assertThat(user.getName(), is(userInDb.getName()));
    }

    @Test
    public void whenUpdateThenFail() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        assertThat(Optional.empty(), is(store.update(user)));
    }

    @Test
    public void whenFindByIdThenOk() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        assertThat(user, is(store.findById(user.getId()).get()));
    }

    @Test
    public void whenFindByIdThenFail() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        assertThat(Optional.empty(), is(store.findById(99)));
    }

    @Test
    public void whenDeleteUserThenOk() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        assertThat(user, is(store.delete(user).get()));
    }

    @Test
    public void whenDeleteUserThenFail() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        UserDBStore store = new UserDBStore(pool);
        assertThat(Optional.empty(), is(store.delete(user)));
    }

    @Test
    public void whenFindAllThenEquals() {
        User user = new User(0, "Dima", "mail@mail.ru", "112");
        User user1 = new User(0, "Ivan", "ivan@mail.ru", "113");
        UserDBStore store = new UserDBStore(pool);
        store.create(user);
        store.create(user1);
        List<User> expected = List.of(user, user1);
        List<User> result = store.findAll();
        assertThat(expected, is(result));
    }
}