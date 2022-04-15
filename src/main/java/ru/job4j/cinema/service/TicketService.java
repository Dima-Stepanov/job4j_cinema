package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.Store;
import ru.job4j.cinema.persistence.TicketDBStore;

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
public class TicketService {
    private final Store<Ticket> store;

    public TicketService(Store<Ticket> store) {
        this.store = store;
    }
}
