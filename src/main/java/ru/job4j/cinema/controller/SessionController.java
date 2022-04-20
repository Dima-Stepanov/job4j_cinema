package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.List;
import java.util.Map;

/**
 * 3. Мидл
 * 3.2.1. WebТопик
 * 3.2.9. Контрольные вопросы
 * 2. Сервис - Кинотеатр [#504869]
 * IndexController. Контроллер главной страницы.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 14.04.2022
 */
@Controller
public class SessionController {
    private final SessionService sessionService;
    private final TicketService ticketService;

    public SessionController(SessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("sessions", sessionService.findAll());
        return "index";
    }

    @GetMapping("/ticketRow/{sessionId}")
    public String formTicket(Model model,
                             @PathVariable("sessionId") int sessionId) {
        if (sessionService.findById(sessionId).isEmpty()) {
            return "redirect:/";
        }
        Map<Integer, List<Ticket>> freeTicket = ticketService.findFreeTicketSession(sessionId);
        model.addAttribute("cinema", sessionService.findById(sessionId).get());
        model.addAttribute("rows", freeTicket);
        return "ticketRow";
    }

    @PostMapping("/ticket")
    public String ticket(@ModelAttribute Ticket ticket) {
        return "redirect:/";
    }
}

