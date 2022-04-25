package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;

import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.TicketDBStore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    /**
     * Количество рядов в зале
     */
    private static final int ROW = 5;
    /**
     * Количество мест в зале
     */
    private static final int CELL = 5;
    /**
     * Хранилище купленных билетов.
     */
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
     * @param sessionId int
     * @return Map
     */
    public Map<Integer, Map<Integer, Ticket>> findTicketSession(int sessionId) {
        return store.findTicketSession(sessionId);
    }

    /**
     * Метод вычисляет карту свободных билетов.
     * Если билет хранится в базе данных значит он куплен.
     *
     * @param session Session
     * @return Map
     */
    public Map<Integer, Map<Integer, Ticket>> findFreeTicketSession(Session session) {
        Map<Integer, Map<Integer, Ticket>> result = initTicket(session);
        Map<Integer, Map<Integer, Ticket>> allTicket = findTicketSession(session.getId());
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
     * @param session
     * @return
     */
    private Map<Integer, Map<Integer, Ticket>> initTicket(Session session) {
        Map<Integer, Map<Integer, Ticket>> result = new HashMap<>();
        for (int i = 1; i <= ROW; i++) {
            result.putIfAbsent(i, new ConcurrentHashMap<>());
            for (int j = 1; j <= CELL; j++) {
                result.get(i).putIfAbsent(j, new Ticket(0, session, i, j, null));
            }
        }
        return result;
    }

    /**
     * Поиск билета по сеансу ряду и месту.
     *
     * @param sessionId id Session
     * @param row       Row ticket
     * @param cell      Cell ticket
     * @return Optionla Ticket.
     */
    public Optional<Ticket> findBySessionRowCell(int sessionId, int row, int cell) {
        return store.findBySessionRowCell(sessionId, row, cell);
    }

    public List<Ticket> findTicketByUser(User user) {
        return store.findTicketByUser(user);
    }
}
