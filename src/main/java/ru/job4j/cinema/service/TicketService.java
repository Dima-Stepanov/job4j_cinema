package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;

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

    /**
     * Поиск билета по ID сеанса.
     *
     * @param sessionId
     * @return
     */
    public Map<Integer, Map<Integer, Ticket>> findTicketSession(int sessionId) {
        return store.findTicketSession(sessionId);
    }

    /**
     * Метод карту свободных билетов.
     * Если билет хранится в базе данных значит он куплен.
     *
     * @param sessionId
     * @return
     */
    public Map<Integer, Map<Integer, Ticket>> findFreeTicketSession(int sessionId) {
        Map<Integer, Map<Integer, Ticket>> result = initTicket(sessionId);
        Map<Integer, Map<Integer, Ticket>> allTicket = findTicketSession(sessionId);
        for (Integer key : allTicket.keySet()) {
            for (Integer k : allTicket.get(key).keySet()) {
                result.get(key).remove(k);
            }
            if (result.get(key).size() == 0) {
                result.remove(key);
            }
        }
        return result;
    }

    /**
     * Заполняем карту пустыми билетами.
     *
     * @param sessionId
     * @return
     */
    private Map<Integer, Map<Integer, Ticket>> initTicket(int sessionId) {
        Map<Integer, Map<Integer, Ticket>> result = new HashMap<>();
        for (int i = 1; i <= ROW; i++) {
            result.put(i, new HashMap<>());
            for (int j = 1; j <= CELL; j++) {
                Ticket ticket = new Ticket(0, new Session(sessionId, null), i, j, null);
                result.get(i).putIfAbsent(j, ticket);
            }
        }
        return result;
    }

    public Optional<Ticket> findBySessionRowCell(int session_id, int row, int cell) {
        return store.findBySessionRowCell(session_id, row, cell);
    }
}
