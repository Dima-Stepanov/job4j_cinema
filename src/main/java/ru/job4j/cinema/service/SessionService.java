package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.persistence.Store;

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
public class SessionService {
    private final Store<Session> store;

    public SessionService(Store<Session> store) {
        this.store = store;
    }
}
