package sk.kosickaakademia.hingis.company.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public String getCurrentDateTime() {

        Date date = new Date();
        long dateNow = date.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(dateNow);
    }
}
