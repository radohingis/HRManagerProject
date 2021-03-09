package sk.kosickaakademia.hingis.company.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.util.Util;

@RestController
public class MainController {


    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable int id) {
        return new Util().parseToJSON(new SQL().getUserById(id));
    }

    @GetMapping("/users")
    public String getAllUsers() {
        return new Util().parseToJSON(new SQL().getAllUsers());
    }
}
