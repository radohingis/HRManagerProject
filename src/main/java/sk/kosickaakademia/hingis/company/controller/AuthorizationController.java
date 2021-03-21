package sk.kosickaakademia.hingis.company.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthorizationController {

    private Map<String, String> tokens = new HashMap<>();

    private final String PASSWORD = "radovesilneheslo";

    @PostMapping("/login")
    public ResponseEntity<String>
    login (@RequestBody String auth_data) {

        JsonObject json_auth_data
                    = new Gson()
                    .fromJson(auth_data, JsonObject.class);

        String login
                = json_auth_data
                .get("login")
                .getAsString();

        String pwd
                = json_auth_data
                .get("pwd")
                .getAsString();

            if(pwd.equals(PASSWORD)) {
                JsonObject jsonObject = new JsonObject();

                String token = new Util().getToken();

                jsonObject.addProperty("login", login);

                tokens.put("login", login);
                tokens.put("token", token);

                jsonObject.addProperty("token", "Bearer " + token);

                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
            }
            else {
                return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
            }
    }

    @GetMapping("/secret")
    public String secret(@RequestHeader("token") String header){

        String token = header.substring(7);

        System.out.println(token);

        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            System.out.println(entry.getValue());
            if(entry.getValue().equals(token)){
                return "secret";
            }
        }
        return "Invalid token";
    }

}
