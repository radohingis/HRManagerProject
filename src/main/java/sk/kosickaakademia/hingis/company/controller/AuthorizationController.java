package sk.kosickaakademia.hingis.company.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.hingis.company.user.Login;
import sk.kosickaakademia.hingis.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthorizationController {

    private Map<String, String> tokens = new HashMap<>();

    private final String PASSWORD = "radovesilneheslo";

    public String getPASSWORD() {
        return PASSWORD;
    }

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

            boolean isUserBlocked = new Login().isUserBlocked(login, pwd);

            if(!isUserBlocked) {

                JsonObject jsonObject = new JsonObject();

                String token = new Util().getToken();

                jsonObject.addProperty("login", login);
                jsonObject.addProperty("token", "Bearer " + token);

                tokens.put(login, token);

                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());

            } else {

                return new ResponseEntity<>("User blocked", HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/secret")
    public String secret(@RequestHeader("token") String header){
        String token = header.substring(7);

        System.out.println(token);

        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            if(entry.getValue().equals(token)){
                return "secret";
            }
        }
        return "Invalid token";
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout (@RequestHeader("token") String header) {
        String token = header.substring(7);

        if(!token.isEmpty()){
            for (Map.Entry<String, String> entry : tokens.entrySet()) {
                if(entry.getValue().equals(token)){
                    tokens.remove(entry.getKey());
                    break;
                }
            }
            return ResponseEntity.ok("Logged out");
        }

        return ResponseEntity.badRequest().body("Something went wrong, you will be redirected to log in page");

    }

    @GetMapping("/privacylimitedinfo")
    public ResponseEntity<String> getData (@RequestBody String body) {

        JsonObject jsonBody = new Gson().fromJson(body, JsonObject.class);



        String token = jsonBody
                        .get("token")
                        .getAsString()
                        .length() > 7

                     ? jsonBody
                        .get("token")
                        .getAsString()
                        .substring(7)

                     : jsonBody
                        .get("token")
                        .getAsString();

        System.out.println(token);

        if (!token.isEmpty()) {
            for (Map.Entry<String, String> entry : tokens.entrySet()) {
                if(entry.getValue().equals(token)) {

                    return ResponseEntity.ok("private data");

                }
            }

        }

            return ResponseEntity.ok("public data");

    }

}
