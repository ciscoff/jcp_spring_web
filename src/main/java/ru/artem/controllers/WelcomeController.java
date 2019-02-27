package ru.artem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class WelcomeController {
    String param = "";

    /**
     * @RequestMapping
     * Фактически это сервлет. В архитектуре MVC он является контроллером, который получает
     * запросы из сети (от Apache), обрабатывает их и генерит результирующую html-страницу (View).
     * Шаблон страницы у нас уже есть в папке templates. Нужно просто передать в неё данные,
     * которые надо отобразить. Делается это через Model.
     * Итого имеем:
     *  Model        - результат обработки web-запроса, помещенный в Map<T,U>
     *  View         - шаблом html-страницы
     *  Controller   - сервлет
     *
     */
    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", "Java!");
        return "welcome";           // Имя возвращаемой страницы html из пакета templates
    }

    @RequestMapping("/about")
    public String about(Map<String, Object> model) {
        model.put("test_param", param);
        return "about";
    }

    @RequestMapping("/news")
    public String news(Map<String, Object> model) {
        model.put("spring", "Spring is great");
        model.put("techno", "technology");
        return "news";
    }

    @RequestMapping("/userinfo")
    public String userinfo(Map<String, Object> model) {
//        model.put("firstname", "Ivan");
//        model.put("lastname", "Petrov");
        return "userinfo";
    }

    @RequestMapping(value = "/signin/", method = RequestMethod.POST)
    public String signin(Map<String, Object> model, @RequestParam("inputEmail") String email, @RequestParam("inputPassword") String password) {
        System.out.println(email + ":" + password);
        model.put("email", email);
        model.put("password", password);
        return "signin";
    }


    @RequestMapping(value = "/printme/{data}", method = RequestMethod.GET)
    public String printme(Map<String, Object> model, @PathVariable("data") String data) {
        model.put("data", data);
        return "printme";
    }

    @RequestMapping(value = "/about/test_param", method = RequestMethod.POST)
    @ResponseBody
    public void addNewCustomer(@RequestParam("name_param") String nameParam) {
        param = nameParam;
        System.out.println(nameParam);
    }

    @RequestMapping(value = "/printme")
    public void printmePut(Map<String, Object> model, String data) {
        model.put("test_param", data);
    }

}
