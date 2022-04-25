package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.Store;
import ru.job4j.cinema.persistence.UserDBStore;

import java.util.List;
import java.util.Optional;

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
@Service
public class UserService {
    private final UserDBStore store;

    public UserService(UserDBStore store) {
        this.store = store;
    }

    public Optional<User> create(User user) {
        return store.create(user);
    }

    public Optional<User> update(User user) {
        return store.update(user);
    }

    public Optional<User> findById(int id) {
        return store.findById(id);
    }

    public List<User> findAll() {
        return store.findAll();
    }

    public Optional<User> findUserByEmailAndPhone(User user) {
        return store.findUserByEmailAndPhone(user.getEmail(), user.getPhone());
    }
}
