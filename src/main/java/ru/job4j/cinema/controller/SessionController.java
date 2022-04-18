package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.SessionService;

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
}

