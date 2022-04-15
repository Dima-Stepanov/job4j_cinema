package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * SessionDBStoreTest. test liquibase.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
public class SessionDBStoreTest {
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
            statement.execute("DELETE FROM sessions;"
                    + "ALTER TABLE sessions ALTER COLUMN session_id RESTART WITH 1");
        }
    }

    @Test
    public void whenCreateSessionDb() {
        Session session = new Session(0, "session1");
        Session session1 = new Session(0, "session2");
        SessionDBStore store = new SessionDBStore(pool);
        store.create(session);
        store.create(session1);
        Session sessionInDb = store.findById(session.getId()).get();
        Session sessionInDb1 = store.findById(session1.getId()).get();
        assertThat(session.getName(), is(sessionInDb.getName()));
        assertThat(session1.getName(), is(sessionInDb1.getName()));
    }

    @Test
    public void whenUpdateSessionDb() {
        Session session = new Session(0, "session");
        SessionDBStore store = new SessionDBStore(pool);
        store.create(session);
        session.setName("new Session");
        store.update(session);
        Session sessionInDb = store.findById(session.getId()).get();
        assertThat(session.getName(), is(sessionInDb.getName()));
    }

    @Test
    public void whenSessionDbFindById() {
        Session session = new Session(0, "s1");
        Session session1 = new Session(0, "s2");
        SessionDBStore store = new SessionDBStore(pool);
        store.create(session);
        store.create(session1);
        assertThat(session1, is(store.findById(session1.getId()).get()));
    }

    @Test
    public void whenDeleteSessionDb() {
        Session session = new Session(0, "s1");
        Session session1 = new Session(0, "s2");
        SessionDBStore store = new SessionDBStore(pool);
        store.create(session);
        store.create(session1);
        Session delSession = store.delete(session).get();
        assertThat(session, is(delSession));
        assertThat(Optional.empty(), is(store.findById(session.getId())));
    }

    @Test
    public void whenSessionDbFindAll() {
        Session session = new Session(0, "s1");
        Session session1 = new Session(0, "s2");
        SessionDBStore store = new SessionDBStore(pool);
        store.create(session);
        store.create(session1);
        List<Session> expected = List.of(session, session1);
        List<Session> result = store.findAll();
        assertThat(expected, is(result));
    }

    @Test
    public void whenSessionDbFindAllEmpty() {
        SessionDBStore store = new SessionDBStore(pool);
        List<Session> expected = List.of();
        List<Session> result = store.findAll();
        assertThat(expected, is(result));
    }
}