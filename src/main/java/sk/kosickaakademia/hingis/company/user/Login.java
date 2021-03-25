package sk.kosickaakademia.hingis.company.user;

import sk.kosickaakademia.hingis.company.controller.AuthorizationController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private Map<String, Integer> attempt = new HashMap<>();
    private Map<String, Long> blocked = new HashMap<>();
    private String pass;


    public Login() {
        this.pass = new AuthorizationController().getPASSWORD();
    }

    public Map<String, Integer> getAttempt() {
        return attempt;
    }

    public Map<String, Long> getBlocked() {
        return blocked;
    }

    public boolean isUserBlocked(String username, String password){
        // 1 je user blokovany
        // ak ano, neuplynul cas blokacie
        // ak uplynul ...odblokuj + over heslo
        // ak cas neuplynul, potom vrat blocked

        // je heslo spravne
        // ak ano generuj a vrat token + vymaz mu zle pokusy ak nejake ma
        // ak je heslo zle, pridaj alebo zvys pocet zlych pokusov
        // ak uz mame 3 zle pokusy, zistim aktualny cas , pridaj 1 min a zapis do blocked + vymaz ho z attemp;

        long currTimeStamp = new Date().getTime();

        if(blocked.containsKey(username) && (currTimeStamp - blocked.get(username)) > 3600 && password == pass) {
            blocked.remove(username);
            return false;
        }

        if(blocked.containsKey(username) && (currTimeStamp - blocked.get(username)) < 3600 && password == pass) {
            return true;
        }

        if(!blocked.containsKey(username) && password != pass) {

            if(!attempt.containsKey(username)){
                attempt.put(username, 1);
            }

            if(attempt.containsKey(username) && attempt.get(username) < 3) {
                attempt.put(username, attempt.get(username) + 1);
            }

            if(attempt.containsKey(username) && attempt.get(username) == 3) {
                attempt.remove(username);
                blocked.put(username, currTimeStamp);
                return true;
            }
        }



        System.out.println("user blocked");
        return false;
    }
}
