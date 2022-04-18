package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cinema.CinemaApplication;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
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
 * TicketDBStoreTest. test liquibase.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
public class TicketDBStoreTest {
    static BasicDataSource pool;

    @BeforeClass
    public static void initPool() {
        pool = new CinemaApplication().loadPool();
        UserDBStore userDBStore = new UserDBStore(pool);
        userDBStore.create(new User(1, "Ivan", "mail@mail.ru", "112"));
        SessionDBStore sessionDBStore = new SessionDBStore(pool);
        sessionDBStore.create(new Session(1, "Стыд"));
    }

    @AfterClass
    public static void closePool() throws SQLException {
        pool.close();
    }

    @After
    public void wipeTableUsers() throws SQLException {
        try (Statement statement = pool.getConnection().createStatement()) {
            statement.execute("DELETE FROM ticket;"
                    + "ALTER TABLE ticket ALTER COLUMN ticket_id RESTART WITH 1");
        }
    }

    @Test
    public void whenCreateTicket() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        store.create(ticket);
        Ticket ticketInDb = store.findById(ticket.getId()).get();
        assertThat(ticket, is(ticketInDb));
    }

    @Test
    public void whenUpdate() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        store.create(ticket);
        ticket.setRow(2);
        ticket.setCell(2);
        store.update(ticket);
        Ticket ticketInDb = store.findById(ticket.getId()).get();
        assertThat(ticket, is(ticketInDb));
    }

    @Test
    public void whenUpdateThenTrue() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        assertThat(Optional.empty(), is(store.update(ticket)));
    }

    @Test
    public void whenFindByIdUser() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        store.create(ticket);
        assertThat(ticket, is(store.findById(ticket.getId()).get()));
    }

    @Test
    public void whenDeleteUser() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        store.create(ticket);
        Ticket ticketInBd = store.delete(ticket).get();
        assertThat(ticket, is(ticketInBd));
    }

    @Test
    public void whenFindAll() {
        Ticket ticket = new Ticket(0, new Session(1, "Стыд"),
                1, 1,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        Ticket ticket1 = new Ticket(0, new Session(1, "Стыд"),
                1, 2,
                new User(1, "Ivan", "mail@mail.ru", "112"));
        TicketDBStore store = new TicketDBStore(pool);
        store.create(ticket);
        store.create(ticket1);
        List<Ticket> expected = List.of(ticket, ticket1);
        List<Ticket> result = store.findAll();
        assertThat(expected, is(result));
    }
}