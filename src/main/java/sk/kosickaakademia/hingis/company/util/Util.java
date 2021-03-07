package sk.kosickaakademia.hingis.company.util;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public String parseToJSON(User user) {
        JsonObject data = new JsonObject();
        JsonElement parsedUser = new Gson().toJsonTree(user);
        if (parsedUser != null) {
            data.add("user", parsedUser);
            data.addProperty("date", getCurrentDateTime());
            return data.toString();
        } else return "{}";
    }

    public String parseToJson(List<User> userList) {

        return null;
    }
}
