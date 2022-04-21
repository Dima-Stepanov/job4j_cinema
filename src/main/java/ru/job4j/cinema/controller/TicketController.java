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
                                @PathVariable("sessionId") int sessionId) {
        if (sessionService.findById(sessionId).isEmpty()) {
            return "redirect:/";
        }
        Map<Integer, Map<Integer, Ticket>> freeTicketRow = ticketService.findFreeTicketSession(sessionId);
        model.addAttribute("cinema", sessionService.findById(sessionId).get());
        model.addAttribute("rows", freeTicketRow.keySet());
        return "ticketRow";
    }

    @GetMapping("/ticketCell/{sessionId}")
    public String formTicketCell(Model model,
                                 @PathVariable("sessionId") int sessionId, @RequestParam("row") int row) {
        Map<Integer, Map<Integer, Ticket>> freeTicketCell = ticketService.findFreeTicketSession(sessionId);
        model.addAttribute("cinema", sessionService.findById(sessionId).get());
        model.addAttribute("row", row);
        model.addAttribute("cells", freeTicketCell.get(row).keySet());
        return "ticketCell";
    }

    @GetMapping("/allTicket")
    public String allTicket(Model model) {
        model.addAttribute("tickets", ticketService.findAllTicket());
        return "allTicket";
    }

    @PostMapping("/saveTicket/{id}/{row}/{cell}")
    public String saveTicket(@ModelAttribute User user,
                             @PathVariable("id") int session_id,
                             @PathVariable("row") int row,
                             @PathVariable("cell") int cell) {
        if (userService.findByEmail(user.getEmail(), user.getPhone()).isEmpty()) {
            userService.create(user);
        } else {
            user = userService.findByEmail(user.getEmail(), user.getPhone()).get();
        }
        if (ticketService.findBySessionRowCell(session_id, row, cell).isEmpty()) {
            Ticket ticket = new Ticket(0, new Session(session_id, ""), row, cell, user);
            ticketService.create(ticket);
        }
        return "redirect:/allTicket";
    }
}
