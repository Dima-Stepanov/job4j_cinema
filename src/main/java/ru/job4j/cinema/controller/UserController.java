package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * UserController. Контроллер регистрации пользователей.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 21.04.2022
 */
@Controller
public class UserController {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final UserService userService;

    public UserController(SessionService sessionService, TicketService ticketService, UserService userService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/loginPage/{sessionId}/{row}")
    public String login(Model model,
                            @PathVariable("sessionId") int sessionId,
                            @PathVariable("row") int row,
                            @RequestParam("cell") int cell) {
        Session cinema = sessionService.findById(sessionId).get();
        Ticket ticket = ticketService.findFreeTicketSession(cinema.getId()).get(row).get(cell);
        model.addAttribute("cinema", cinema);
        model.addAttribute("ticket", ticket);
        return "login";
    }


}
