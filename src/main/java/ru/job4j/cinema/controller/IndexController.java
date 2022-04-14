package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}

