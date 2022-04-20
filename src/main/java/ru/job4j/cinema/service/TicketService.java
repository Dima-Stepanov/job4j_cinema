package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.Store;
import ru.job4j.cinema.persistence.TicketDBStore;

import java.util.*;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * TicketService. Слой сервиса Ticket.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
@Service
public class TicketService {
    private static final int ROW = 5;
    private static final int CELL = 5;
    private final TicketDBStore store;

    public TicketService(TicketDBStore store) {
        this.store = store;
    }

    public Optional<Ticket> create(Ticket ticket) {
        return store.create(ticket);
    }

    public Optional<Ticket> findById(int idTicket) {
        return store.findById(idTicket);
    }

    public Optional<Ticket> delete(Ticket ticket) {
        return store.delete(ticket);
    }

    public List<Ticket> findAllTicket() {
        return store.findAll();
    }

    public Map<Integer, List<Ticket>> findTicketSession(int sessionId) {
        return store.findTicketSession(sessionId);
    }

    public Map<Integer, List<Ticket>> findFreeTicketSession(int sessionId) {
        Map<Integer, List<Ticket>> result = initTicket(sessionId);
        Map<Integer, List<Ticket>> allTicket = findTicketSession(sessionId);
        for (Integer key : allTicket.keySet()) {
            result.computeIfPresent(key, (k, v) -> {
                v.removeAll(allTicket.get(key));
                return v;
            });
        }
        return result;
    }

    private Map<Integer, List<Ticket>> initTicket(int sessionId) {
        Map<Integer, List<Ticket>> result = new HashMap<>();
        for (int i = 1; i <= ROW; i++) {
            result.putIfAbsent(i, new ArrayList<>());
            for (int j = 1; j <= CELL; j++) {
                Ticket ticket = new Ticket(0, new Session(sessionId, null), i, j, null);
                result.computeIfPresent(ticket.getRow(), (k, v) -> {
                    v.add(ticket);
                    return v;
                });
            }
        }
        return result;
    }
}
