package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
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
    private final Map<Session,
            ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Integer>>> selectTicket = new ConcurrentHashMap<>();

    public SessionService(SessionDBStore store) {
        this.store = store;
        initTicket();
    }

    private void initTicket() {
        int row = 5;
        int cell = 5;
        List<Session> sessions = store.findAll();
        for (Session session : sessions) {
            this.selectTicket.computeIfAbsent(session, s -> {
                ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Integer>> rows = new ConcurrentHashMap<>();
                for (int i = 1; i <= row; i++) {
                    rows.computeIfAbsent(i, k -> {
                        ConcurrentLinkedQueue<Integer> cells = new ConcurrentLinkedQueue<>();
                        for (int j = 1; j <= cell; j++) {
                            cells.add(j);
                        }
                        return cells;
                    });
                }
                return rows;
            });
        }
    }

    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Integer>> findAllTicket(Session session) {
        return this.selectTicket.get(session);
    }

    public Optional<Session> findById(int id) {
        return store.findById(id);
    }

    public List<Session> findAll() {
        return store.findAll();
    }

    public void test() {
        Session session = new Session(0, "ddd");
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Integer>> l = findAllTicket(session);
        for (ConcurrentLinkedQueue<Integer> element : l.values()) {
            for (Integer i : element) {
                System.out.println(i);
            }
        }
    }
}
