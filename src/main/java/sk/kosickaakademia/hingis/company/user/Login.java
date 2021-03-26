package sk.kosickaakademia.hingis.company.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private Map<String, Integer> attempt = new HashMap<>();
    private Map<String, Long> blocked = new HashMap<>();

    private final String PASSWORD = "radovesilneheslo";

    long currTimeStamp = new Date().getTime();

    public boolean isUserBlocked(String username, String password){

        //if user won't pass security check

        if(securityCheckPassed(username, password)) {

            //we wan't to know if his blocking is still active

            //if yes then we remove his registration from black list (e.g. blocked)
            //returning false as a state for 'is user blocked?'

            if(blocked.containsKey(username) && (currTimeStamp - blocked.get(username)) >= 3600) {
                blocked.remove(username);
                return false;
            }

            //but if the blocking is still true
            //we are returning true as a state for 'is user blocked?'

            if(blocked.containsKey(username) && (currTimeStamp - blocked.get(username)) < 3600) {
                return true;
            }
        }

        return true;
    }

    public boolean securityCheckPassed(String username, String password) {

        //if user is not in black list (e.g. blocked)

        if(!blocked.containsKey(username) && password != PASSWORD) {

            //then we wan't to get know if he has already been trying to connect
            //if no, we put him in map and set his attempt to 1 initially
            //returning true as he is not blocked already

            if(!attempt.containsKey(username)){
                attempt.put(username, 1);
                return true;
            }

            //if user already is in attempts register
            //for security we start counting his attempts
            //and returning true until attempts are in total less than 3

            if(attempt.containsKey(username) && attempt.get(username) < 3) {
                attempt.put(username, attempt.get(username) + 1);
                return true;
            }

            //here, checking if user already reached 3 attempts
            //when so => we move him in black list (e.g. blocked)

            if(attempt.containsKey(username) && attempt.get(username) == 3) {
                attempt.remove(username);
                blocked.put(username, currTimeStamp);
                return false;
            }
        }
        return false;
    }
}
