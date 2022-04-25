package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.desktop.OpenFilesEvent;
import java.util.Optional;

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
                        @RequestParam("cell") int cell,
                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, "Гость", "Гость", "Гость");
        }
        model.addAttribute("user", user);
        Session cinema = sessionService.findById(sessionId).get();
        Ticket ticket = ticketService.findFreeTicketSession(cinema).get(row).get(cell);
        Optional<Ticket> expectTicket = ticketService.findBySessionRowCell(sessionId, row, cell);
        if (expectTicket.isPresent()) {
            return "redirect:/sessionFail?fail=true";
        }
        model.addAttribute("cinema", cinema);
        model.addAttribute("ticket", ticket);
        return "login";
    }

    @GetMapping("/userPage")
    public String userPage(Model model,
                           @RequestParam(name = "fail", required = false) Boolean fail,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, "Гость", "Гость", "Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("fail", fail != null);
        return "/user";
    }

    @PostMapping("/userIn")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByEmailAndPhone(user);
        if (userDb.isEmpty()) {
            return "redirect:/userPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/";
    }

    @GetMapping("/userOut")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
