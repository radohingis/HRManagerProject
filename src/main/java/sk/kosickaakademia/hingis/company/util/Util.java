package sk.kosickaakademia.hingis.company.util;


import com.google.gson.*;
import com.mysql.cj.xdevapi.JsonNumber;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sk.kosickaakademia.hingis.company.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Util {

    public String getCurrentDateTime() {

        Date date = new Date();
        long dateNow = date.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(dateNow);
    }

    public String parseToJSON(User u) {
        JsonObject data = new JsonObject();
        JsonElement user = new Gson().toJsonTree(u);
        if (user != null) {
            data.add("user", user);
            data.addProperty("date", getCurrentDateTime());

            return new GsonBuilder().create().toJson(data);

        } else

            return "{}";
    }

    public String parseToJSON(List<User> userList) {
        if (userList.size() != 0) {
            JsonObject data = new JsonObject();
            JsonArray usersArr = new JsonArray();

            for (User u : userList) {
                usersArr.add(new Gson().toJsonTree(u));
            }

            data.add("user", usersArr);
            data.addProperty("date", getCurrentDateTime());
            data.addProperty("num_of_results", userList.size());

            return new GsonBuilder().create().toJson(data);

        } else

            return "{}";
    }

    public JsonObject message (String type, String message) {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty(type, message);
        responseMessage.addProperty("date", new Util().getCurrentDateTime());
        return responseMessage;
    }

    public JsonObject emptyArray () {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty("date", getCurrentDateTime());
        responseMessage.addProperty("size", 0);
        responseMessage.addProperty("user", "[]");
        return responseMessage;
    }

    public String getToken () {
        String token = "";
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            int acc = random.nextInt(3);
            switch (acc) {

                case 0: token = token + (char) (random.nextInt(26)+65); break;
                case 1: token = token + (char) (random.nextInt(26)+97); break;
                case 2: token = token + (char) (random.nextInt(10)+48); break;

            }
        }

        return token;

    }
}
