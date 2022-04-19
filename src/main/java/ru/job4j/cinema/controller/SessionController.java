package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

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
    private final SessionService serviceService;

    public SessionController(SessionService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("sessions", serviceService.findAll());
        return "index";
    }

    @GetMapping("/selectTicket/{sessionId}")
    public String formSelectTicket(Model model,
                                   @PathVariable("sessionId") int id) {
        model.addAttribute("cinema", serviceService.findById(id).get());
        return "selectTicket";
    }

    @PostMapping("/selectTicket")
    public String selectTicket(@ModelAttribute Ticket ticket) {
        return "redirect:index";
    }
}

