package sk.kosickaakademia.hingis.company.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sk.kosickaakademia.hingis.company.util.Util;

@RestController
public class AuthorizationController {

    private final String PASSWORD = "radovesilneheslo";

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody String auth_data){

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

                jsonObject.addProperty("token", "Bearer " + token);

                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
            }
            else {
                return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
            }
    }

}
