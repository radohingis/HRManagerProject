package sk.kosickaakademia.hingis.company.controller;

import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.util.Util;

import java.util.List;

@RestController
public class MainController {


    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserById(@PathVariable int id) {
        User user = new SQL().getUserById(id);
        if(user != null) {
            String userJson = new Util().parseToJSON(user);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(userJson);
        }
        JsonObject error = new JsonObject();
        error.addProperty("message", "user not found, bad request");
        error.addProperty("date", new Util().getCurrentDateTime());
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(error.toString());
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        List<User> users = new SQL().getAllUsers();
        if(users.size() >= 1) {
            String usersJson = new Util().parseToJSON(users);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(usersJson);
        }
        JsonObject error = new JsonObject();
        error.addProperty("message", "something went wrong, be patient please");
        error.addProperty("date", new Util().getCurrentDateTime());
        return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(error.toString());
    }
}
