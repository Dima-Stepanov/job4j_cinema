package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.SessionDBStore;
import ru.job4j.cinema.persistence.Store;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * SessionService слой сервиса Session.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
@Service
public class SessionService {
    private final Store<Session> store;
    private static final Integer ROW = 5;
    private static final Integer CELL = 5;
    private final Map<Session, Ticket[]> tickets = new ConcurrentHashMap<>();

    public SessionService(SessionDBStore store) {
        this.store = store;
        initTicket();
    }

    private void initTicket() {
        List<Session> sessions = store.findAll();
        for (Session session : sessions) {
            tickets.computeIfAbsent(session, k -> {
                Ticket[] ticket = new Ticket[ROW * CELL];
                int step = 0;
                for (int i = 1; i <= ROW; i++) {
                    for (int j = 1; j <= CELL; j++) {
                        ticket[step++] = new Ticket(0, k, i , j, null);
                    }
                }
                return ticket;
            });
        }

    }

    public Ticket[] getTickets(Session session) {
        return tickets.get(session);
    }

    public Optional<Session> findById(int id) {
        return store.findById(id);
    }

    public List<Session> findAll() {
        return store.findAll();
    }
}
