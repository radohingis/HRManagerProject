package sk.kosickaakademia.hingis.company.controller;

import com.google.gson.*;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.enumerator.Gender;
import sk.kosickaakademia.hingis.company.util.Util;

import java.lang.reflect.Field;
import java.util.*;

@RestController
public class MainController {



    private ResponseEntity
            .BodyBuilder OKrequest
            = ResponseEntity
            .status(200)
            .contentType(MediaType.APPLICATION_JSON);

    private ResponseEntity
            .BodyBuilder badRequest
            = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_JSON);

    private ResponseEntity
            .BodyBuilder notFound
            = ResponseEntity
            .status(404)
            .contentType(MediaType.APPLICATION_JSON);


    @GetMapping("/user/{id}")
    public ResponseEntity<String>
    getUserById(@PathVariable int id) {

        User user = new SQL().getUserById(id);

        if (user != null) {

            String data = new Util().parseToJSON(user);

            return OKrequest.body(data);
        }
        return notFound.body(new Util()
                .message("error", "user with ID does not exist")
                .toString());
    }

    @GetMapping("/users")
    public ResponseEntity<String>
    getAllUsers() {

        List<User> users = new SQL().getAllUsers();

        if (users.size() >= 1) {
            String data = new Util().parseToJSON(users);

            return OKrequest.body(data);
        } else {
            return OKrequest.body(new Util()
                    .emptyArray()
                    .toString()
            );
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<String>
    addUser(@RequestBody String data) {

        User user = new Gson().fromJson(data, User.class);

        if (user.getLname().equals("")
                || user.getFname().equals("")
                || user.getAge() < 1
                || user.getAge() > 99) return badRequest
                .body(new Util()
                        .message("error",
                                "incorrect body, missing lname or fname or age,"
                                        + "age is zero or negative number")
                        .toString());

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
                    .status(201)
                    .body(new Util()
                            .message("success",
                                    "user created")
                            .toString()
                    );
        }
    }

    @GetMapping(
            value = "/users",
            params = {"fromAge", "toAge"})
    public ResponseEntity<String>
    getUsersByAgeRange(
            @RequestParam(value = "fromAge",
                    defaultValue = "1")
                    int from,

            @RequestParam(value = "toAge",
                    defaultValue = "99")
                    int to) {


        if (from < 1
                || to > 99
                || from > to) return badRequest.body(new Util()
                .message("error",
                        "incorrect path, missing from/to or from is bigger than to").toString());

        List<User> userList
                = new SQL()
                .selectRangeBasedOnUserAge(from, to);

        if (userList.size() >= 1) {

            String data = new Util().parseToJSON(userList);

            return OKrequest.body(data);

        } else {
            JsonObject data = new Util().emptyArray();
            return OKrequest.body(data.toString());

        }
    }


    @GetMapping(
            value = "/users",
            params = "gender")
    public ResponseEntity<String>
    getUsersByGender(
            @RequestParam(value = "gender",
                    required = false,
                    defaultValue = "")
                    String gender) {
        if (gender.equals("")) return getAllUsers();

        if (
                !(gender.equals("male")
                        || gender.equals("female")
                        || gender.equals("other"))

        ) return badRequest.body("{}");
        else {
            List<User> userList
                    = new SQL().selectByGender(
                    gender.equals("male") ? 0
                            : gender.equals("female") ? 1
                            : gender.equals("other") ? 2 : 2
            );
            if (userList.size() >= 1) {
                String data = new Util().parseToJSON(userList);
                return OKrequest.body(data);
            } else {
                return OKrequest.body(new Util().emptyArray().toString());
            }
        }
    }


    @PutMapping("/user/{id}/age/update")
    public ResponseEntity<String>
    updateUserAge(
            @PathVariable int id,
            @RequestBody String data) {
        if (!data.isEmpty()) {
            JsonObject jsonData
                    = new Gson()
                    .fromJson(data, JsonObject.class);

            if (jsonData.has("age")) {

                String age
                        = jsonData
                        .get("age")
                        .toString();

                if (!age.isEmpty()) {

                    new SQL().updateUser(id, "age", age);

                    return ResponseEntity
                            .status(204)
                            .body(null);

                }
            } else {
                return badRequest
                        .body(new Util()
                                .message(
                                        "error",
                                        "received wrong/no key")
                                .toString());
            }
        }
        return null;
    }


}
