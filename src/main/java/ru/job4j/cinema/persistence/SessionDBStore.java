package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * SessionDBStore. Хранилище сеансов.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
@Repository
public class SessionDBStore implements Store<Session> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDBStore.class.getName());
    private final BasicDataSource pool;

    public SessionDBStore(BasicDataSource pool) {
        this.pool = pool;
        initScheme();
    }

    /**
     * Инициализация таблицы сеансов SESSIONS.
     */
    private void initScheme() {
        LOGGER.info("Инициализация таблицы sessions");
        try (Statement statement = pool.getConnection().createStatement()) {
            String sql = Files.readString(Path.of("db/scripts", "sessions.sql"));
            statement.execute(sql);
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
        }
    }

    /**
     * Создание сеанса.
     *
     * @param session Session.
     * @return Optional.
     */
    @Override
    public Optional<Session> create(Session session) {
        LOGGER.info("Сохранение сеанса {}:{}", session.getId(), session.getName());
        String sql = ("INSERT INTO sessions(session_name) VALUES (?);");
        Optional<Session> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, session.getName());
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    session.setId(resultSet.getInt(1));
                    result = Optional.of(session);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Обновление записи сеансов
     *
     * @param session Session.
     * @return Optional.
     */
    @Override
    public Optional<Session> update(Session session) {
        LOGGER.info("Обновление сеанса {}:{}", session.getId(), session.getName());
        String sql = "UPDATE sessions SET session_name = ? WHERE session_id = ?;";
        Optional<Session> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(2, session.getId());
            statement.setString(1, session.getName());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(session);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Поиск сеанса по ID.
     *
     * @param id Integer
     * @return Optional.
     */
    @Override
    public Optional<Session> findById(int id) {
        LOGGER.info("Поиск сеанса по ID:{}", id);
        String sql = "SELECT * FROM sessions WHERE session_id = ?;";
        Optional<Session> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(getSession(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Удаление сеанса.
     *
     * @param session Session.
     * @return Optional.
     */
    @Override
    public Optional<Session> delete(Session session) {
        LOGGER.info("Удаление сеанса {}:{}", session.getId(), session.getName());
        String sql = "DELETE FROM sessions WHERE session_id = ?;";
        Optional<Session> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, session.getId());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(session);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Поиск всех сеансов.
     *
     * @return ArrayList.
     */
    @Override
    public Collection<Session> findAll() {
        LOGGER.info("Создание списка всех сеансов");
        List<Session> sessionList = new ArrayList<>();
        String sql = "SELECT * FROM sessions;";
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sessionList.add(getSession(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return sessionList;
    }

    /**
     * Возвращает Session из ResultSet.
     *
     * @param resultSet ResultSet
     * @return Session
     * @throws SQLException exception.
     */
    private Session getSession(ResultSet resultSet) throws SQLException {
        return new Session(resultSet.getInt(1),
                resultSet.getString(2));
    }
}
