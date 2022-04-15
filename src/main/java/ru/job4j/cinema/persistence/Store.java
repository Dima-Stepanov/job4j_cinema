package ru.job4j.cinema.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * Store интерфейс описывает поведение хранилища.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
public interface Store<T> {

    Optional<T> create(T type);

    Optional<T> update(T type);

    Optional<T> findById(int id);

    Optional<T> delete(T type);

    List<T> findAll();
}
