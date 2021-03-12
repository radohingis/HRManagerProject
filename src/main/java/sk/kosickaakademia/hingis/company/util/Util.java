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
}
