package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.Store;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * UserService слой сервиса User.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 15.04.2022
 */
public class UserService {
    private final Store<User> store;

    public UserService(Store<User> store) {
        this.store = store;
    }
}
