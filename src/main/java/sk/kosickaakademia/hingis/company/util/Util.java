package sk.kosickaakademia.hingis.company.util;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.kosickaakademia.hingis.company.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public String getCurrentDateTime() {

        Date date = new Date();
        long dateNow = date.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(dateNow);
    }

    public String parseToJSON(User user) {
        JsonObject data = new JsonObject();
        data.addProperty("date", getCurrentDateTime());
        data.add("user", new Gson().toJsonTree(user));
        return data.toString();
    }
}
