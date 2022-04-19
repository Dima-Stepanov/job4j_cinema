package ru.job4j.cinema.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cinema.CinemaApplication;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.SessionDBStore;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.*;

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

    @Test
    public void whenInitTest() {
        SessionDBStore store = new SessionDBStore(pool);
        Session session = new Session(0, "Ну погоди");
        Session session1 = new Session(0, "Волки в лесу");
        store.create(session);
        store.create(session1);
        SessionService service = new SessionService(store);
        Ticket[] tickets = service.getTickets(session);
        for (Ticket ticket : tickets) {
            System.out.printf("row-{%d} \n\r", ticket.getRow());
        }
        for (Ticket ticket : tickets) {
            System.out.printf("cell-{%d} \n\r", ticket.getCell());
        }
    }
}