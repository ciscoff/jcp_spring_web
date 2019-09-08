package ru.artem.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.RequestDispatcher;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Controller
public class WelcomeController {
    Connection conn = null;
    Statement stat = null;
    String param = "";

    public WelcomeController() {

        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:lesson8.db");
            this.stat = conn.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @RequestMapping Фактически это сервлет. В архитектуре MVC он является контроллером, который получает
     * запросы из сети (от Apache), обрабатывает их и генерит результирующую html-страницу (View).
     * Шаблон страницы у нас уже есть в папке templates. Нужно просто передать в неё данные,
     * которые надо отобразить. Делается это через Model.
     * Итого имеем:
     * Model        - результат обработки web-запроса, помещенный в Map<T,U>
     * View         - шаблом html-страницы
     * Controller   - сервлет
     */
//    @RequestMapping("/")
//    public String welcome(Map<String, Object> model) {
//        model.put("message", "Java!");
//        return "welcome";           // Имя возвращаемой страницы html из пакета templates
//    }

//    @RequestMapping("/about")
//    public String about(Map<String, Object> model) {
//        model.put("test_param", param);
//        return "about";
//    }
//
//    @RequestMapping("/news")
//    public String news(Map<String, Object> model) {
//        model.put("spring", "Spring is great");
//        model.put("techno", "technology");
//        return "news";
//    }
//
//    /**
//     * Redirect to userinfo.html page
//     * @return userinfo.html
//     */
//    @RequestMapping("/userinfo")
//    public String userinfo() {
//        return "userinfo";
//    }


    /**
     * Read data from SQlite and create HTML-table
     * @return table
     */
    @RequestMapping("/")
    public String load(Map<String, Object> model) {
        ArrayList<String> al = new ArrayList<>();

        try {
            ResultSet rs = stat.executeQuery("select id, lastname, name, extension from persons");

            al.add("<table class=\"table\">\n" +
                    "<thead class=\"thead-dark\">\n" +
                    "<tr>\n" +
                    "<th scope=\"col\">#</th>\n" +
                    "<th scope=\"col\">Фамилия</th>\n" +
                    "<th scope=\"col\">Имя</th>\n" +
                    "<th scope=\"col\">Отчество</th>\n" +
                    "</tr>\n" +
                    "</thead>\n" +
                    "<tbody>\n");

            while (rs.next()) {
                al.add("<tr>");
                al.add("<th scope=\"row\">" + rs.getString("id") + "</td>\n");
                al.add("<td>" + rs.getString("lastname") + "</td>\n");
                al.add("<td>" + rs.getString("name") + "</td>\n");
                al.add("<td>" + rs.getString("extension") + "</td>\n");
                al.add("</tr>\n");
            }

            al.add("</tbody>\n</table>");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            model.put("persons", String.join("", al.toArray(new String[al.size()])));
        }

        return "persons";
    }

    /**
     * Put data to SQlite
     * @return Call load() to redirect to persons.html
     */
    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public String store(Map<String, Object> model,
                        @RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName,
                        @RequestParam("extName") String extName) {

        String sql = "insert into persons (name, lastname, extension) values (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, extName);

            System.out.printf("%d rows added", preparedStatement.executeUpdate());

        } catch (SQLException e) {e.printStackTrace();}

        /**
         * Redirect to persons.html
         */
        return load(model);
    }


//    @RequestMapping(value = "/signin/", method = RequestMethod.POST)
//    public String signin(Map<String, Object> model, @RequestParam("inputEmail") String email, @RequestParam("inputPassword") String password) {
//        System.out.println(email + ":" + password);
//        model.put("email", email);
//        model.put("password", password);
//        return "signin";
//    }
//
//
//    @RequestMapping(value = "/printme/{data}", method = RequestMethod.GET)
//    public String printme(Map<String, Object> model, @PathVariable("data") String data) {
//        model.put("data", data);
//        return "printme";
//    }
//
//    @RequestMapping(value = "/about/test_param", method = RequestMethod.POST)
//    @ResponseBody
//    public void addNewCustomer(@RequestParam("name_param") String nameParam) {
//        param = nameParam;
//        System.out.println(nameParam);
//    }
//
//    @RequestMapping(value = "/printme")
//    public void printmePut(Map<String, Object> model, String data) {
//        model.put("test_param", data);
//    }
}
