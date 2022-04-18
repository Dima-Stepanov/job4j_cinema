package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.persistence.SessionDBStore;
import ru.job4j.cinema.persistence.Store;

import java.util.List;

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

    public SessionService(SessionDBStore store) {
        this.store = store;
    }

    public List<Session> findAll() {
        return store.findAll();
    }
}
