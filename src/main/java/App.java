import sk.kosickaakademia.hingis.company.database.SQL;
import sk.kosickaakademia.hingis.company.entity.User;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        SQL sql = new SQL();
        sql.selectRangeBasedOnAge(0, 40);
    }
}
