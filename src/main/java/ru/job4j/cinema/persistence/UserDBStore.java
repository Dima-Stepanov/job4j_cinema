package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * UserDBStore. Хранилище пользователей.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
@Repository
public class UserDBStore implements Store<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDBStore.class.getName());
    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    /**
     * Создание пользователя.
     *
     * @param user Users
     * @return Optional.
     */
    @Override
    public Optional<User> create(User user) {
        LOGGER.info("Сохранение пользователя {}:{}", user.getId(), user.getEmail());
        String sql = "INSERT INTO users(user_name, email, phone) VALUES (?, ?, ?);";
        Optional<User> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                    result = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию {}", e.getMessage());
        }
        return result;
    }

    /**
     * Обновление записи пользователя.
     *
     * @param user User
     * @return Optional.
     */
    @Override
    public Optional<User> update(User user) {
        LOGGER.info("Обновление пользователя {}:{}", user.getId(), user.getEmail());
        String sql = "UPDATE users SET user_name = ?, email = ?, phone = ? WHERE user_id = ?;";
        Optional<User> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setInt(4, user.getId());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(user);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getMessage());
        }
        return result;
    }

    /**
     * Поиск пользователя по ID.
     *
     * @param id Int.
     * @return Optional.
     */
    @Override
    public Optional<User> findById(int id) {
        LOGGER.info("Поиск пользователя по ID:{}", id);
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        Optional<User> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(getUser(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getMessage());
        }
        return result;
    }

    /**
     * Удаление пользователя.
     *
     * @param user User.
     * @return Optional.
     */
    @Override
    public Optional<User> delete(User user) {
        LOGGER.info("Удаление пользователя {}:{}", user.getId(), user.getEmail());
        String sql = "DELETE FROM users WHERE user_id = ? AND email = ? AND phone = ?;";
        Optional<User> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(user);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getMessage());
        }
        return result;
    }

    /**
     * Поиск всех сеансов.
     *
     * @return ArrayList.
     */
    @Override
    public List<User> findAll() {
        LOGGER.info("Создание списка всех пользователей");
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users;";
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(getUser(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getMessage());
        }
        return userList;
    }

    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        LOGGER.info("Поиск пользователя по email:{} phone:{}", email, phone);
        String sql = "SELECT * FROM users WHERE email = ? AND phone = ?;";
        Optional<User> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, phone);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(getUser(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getMessage());
        }
        return result;
    }

    /**
     * Возвращает User из ResultSet.
     *
     * @param resultSet ResultSet.
     * @return User.
     * @throws SQLException exception.
     */
    private User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("user_id"),
                resultSet.getString("user_name"),
                resultSet.getString("email"),
                resultSet.getString("phone"));
    }
}
