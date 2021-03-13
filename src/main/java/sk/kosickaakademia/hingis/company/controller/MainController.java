package sk.kosickaakademia.hingis.company.controller;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.enumerator.Gender;
import sk.kosickaakademia.hingis.company.util.Util;

import java.util.List;

@RestController
public class MainController {

    private JsonObject badRequestMessage
                        = new Util()
                        .message("error", "badrequest");

    private ResponseEntity badRequest
                                    = ResponseEntity
                                    .status(400)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(badRequestMessage);

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserById(@PathVariable int id) {

        User user = new SQL().getUserById(id);

        if(user != null) {

            String userJson = new Util().parseToJSON(user);

            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userJson);
        }
        return badRequest;
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {

        List<User> users = new SQL().getAllUsers();

        if(users.size() >= 1) {
            String usersJson = new Util().parseToJSON(users);

            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(usersJson);
        }
        return badRequest;
    }

    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody String data) {

        User user = new Gson().fromJson(data, User.class);

        if(user.getLname().equals("")
        || user.getFname().equals("")
        || user.getAge() < 1
        || user.getAge() > 99) return badRequest;

        else {
            Gender gender = (user.getGender() == null)
                    || (user.getGender().getValue() == 2)
                    ? Gender.other //if body doesn't contain gender key, request passes object with gender: other (2)
                    : user.getGender().getValue() == 1
                    ? Gender.female
                    : Gender.male;

            User validUser = new User(
                    user.getFname(),
                    user.getLname(),
                    Integer.parseInt(String.valueOf(user.getAge())),
                    gender.getValue()
            );

            new SQL().insertNewUser(validUser);

            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }
    }
}
