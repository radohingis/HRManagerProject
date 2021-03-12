package sk.kosickaakademia.hingis.company.controller;

import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.util.Util;

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
    public String getAllUsers() {
        return new Util().parseToJSON(new SQL().getAllUsers());
    }
}
