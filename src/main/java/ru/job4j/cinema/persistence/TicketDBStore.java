package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

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
 * TicketDBStore. Хранилище пользователей.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
public class TicketDBStore implements Store<Ticket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDBStore.class.getName());

    private final BasicDataSource pool;


    public TicketDBStore(BasicDataSource pool) {
        this.pool = pool;
        initScheme();
    }

    /**
     * Инициализация таблицы билетов TICKET.
     */
    private void initScheme() {
        LOGGER.info("Инициализация таблицы ticket");
        try (Statement statement = pool.getConnection().createStatement()) {
            String sql = Files.readString(Path.of("db/scripts", "ticket.sql"));
            statement.execute(sql);
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
    }

    /**
     * Создание билета.
     *
     * @param ticket Ticket
     * @return Optional.
     */
    @Override
    public Optional<Ticket> create(Ticket ticket) {
        LOGGER.info("Сохранение билета {}:{}", ticket.getId(), ticket.getSession().getName());
        String sql = ("INSERT INTO ticket (session_id, rowt, cell, user_id) VALUES (?, ?, ?, ?);");
        Optional<Ticket> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ticket.getSession().getId());
            statement.setInt(2, ticket.getRow());
            statement.setInt(3, ticket.getCell());
            statement.setInt(4, ticket.getUser().getId());
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    ticket.setId(resultSet.getInt(1));
                    result = Optional.of(ticket);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Обновление билета.
     *
     * @param ticket Ticket
     * @return optional.
     */
    @Override
    public Optional<Ticket> update(Ticket ticket) {
        LOGGER.info("Обновление билета {}:{}", ticket.getId(), ticket.getSession().getName());
        String sql = "UPDATE ticket SET session_id = ?, rowt = ?, cell = ?, user_id = ? WHERE ticket_id = ?;";
        Optional<Ticket> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(5, ticket.getId());
            statement.setInt(1, ticket.getSession().getId());
            statement.setInt(2, ticket.getRow());
            statement.setInt(3, ticket.getCell());
            statement.setInt(4, ticket.getUser().getId());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(ticket);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Поиск билета по ID.
     *
     * @param id Int.
     * @return Optional.
     */
    @Override
    public Optional<Ticket> findById(int id) {
        LOGGER.info("Поиск сеанса по ID:{}", id);
        String sql = "SELECT * FROM ticket AS t "
                + "INNER JOIN users AS u "
                + "USING (user_id) "
                + "INNER JOIN sessions AS s "
                + "USING (session_id) "
                + "WHERE t.ticket_id = ?;";
        Optional<Ticket> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(getTicket(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Удаление билета.
     *
     * @param ticket Ticket.
     * @return Optional.
     */
    @Override
    public Optional<Ticket> delete(Ticket ticket) {
        LOGGER.info("Удаление билета {}:{}", ticket.getId(), ticket.getSession().getName());
        String sql = "DELETE FROM ticket WHERE ticket_id = ?;";
        Optional<Ticket> result = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ticket.getId());
            if (statement.executeUpdate() > 0) {
                result = Optional.of(ticket);
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return result;
    }

    /**
     * Поиск всех билетов.
     *
     * @return ArrayList.
     */
    @Override
    public List<Ticket> findAll() {
        LOGGER.info("Создание списка всех билетов");
        List<Ticket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM ticket AS t "
                + "INNER JOIN users AS u "
                + "USING (user_id) "
                + "INNER JOIN sessions AS s "
                + "USING (session_id);";
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ticketList.add(getTicket(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Не удалось выполнить операцию { }", e.getCause());
        }
        return ticketList;
    }

    /**
     * Возвращает Ticket из ResultSet
     *
     * @param resultSet ResultSet
     * @return User
     * @throws SQLException
     */
    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getInt("ticket_id"),
                new Session(resultSet.getInt("session_id"),
                        resultSet.getString("session_name")),
                resultSet.getInt("rowt"),
                resultSet.getInt("cell"),
                new User(resultSet.getInt("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")));
    }
}
