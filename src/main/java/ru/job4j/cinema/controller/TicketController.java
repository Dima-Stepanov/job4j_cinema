package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * TicketController. Контроллер регистрации билетов.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 21.04.2022
 */
@Controller
public class TicketController {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(SessionService sessionService, TicketService ticketService, UserService userService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/ticketRow/{sessionId}")
    public String formTicketRow(Model model,
                                @PathVariable("sessionId") int sessionId,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, "Гость", "Гость", "Гость");
        }
        model.addAttribute("user", user);
        Map<Integer, Map<Integer, Ticket>> freeTicketRow = ticketService.findFreeTicketSession(new Session(sessionId, null));
        model.addAttribute("cinema", sessionService.findById(sessionId).get());
        model.addAttribute("rows", freeTicketRow.keySet());
        return "ticketRow";
    }

    @GetMapping("/ticketCell/{sessionId}")
    public String formTicketCell(Model model,
                                 @PathVariable("sessionId") int sessionId,
                                 @RequestParam("row") int row,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, "Гость", "Гость", "Гость");
        }
        model.addAttribute("user", user);
        Map<Integer, Map<Integer, Ticket>> freeTicketCell = ticketService.findFreeTicketSession(new Session(sessionId, null));
        model.addAttribute("cinema", sessionService.findById(sessionId).get());
        model.addAttribute("row", row);
        model.addAttribute("cells", freeTicketCell.get(row).keySet());
        return "ticketCell";
    }

    @GetMapping("/allTicket")
    public String allTicket(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, "Гость", "Гость", "Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("tickets", ticketService.findTicketByUser(user));
        return "allTicket";
    }

    @PostMapping("/saveTicket/{sessionId}/{row}/{cell}")
    public String saveTicket(@ModelAttribute User user,
                             @PathVariable("sessionId") int sessionId,
                             @PathVariable("row") int row,
                             @PathVariable("cell") int cell,
                             Model model,
                             HttpSession session) {
        Optional<User> regUser = userService.create(user);
        if (regUser.isEmpty()) {
            regUser = userService.findUserByEmailAndPhone(user);
        }
        if (regUser.isEmpty()) {
            return "redirect:/?failUser=true";
        }
        session.setAttribute("user", regUser.get());
        model.addAttribute("user", regUser.get());
        Optional<Ticket> ticket = ticketService.create(new Ticket(0, new Session(sessionId, ""), row, cell, regUser.get()));
        if (ticket.isEmpty()) {
            return "redirect:/?failTicket=true";
        }
        return "redirect:/allTicket";
    }
}
