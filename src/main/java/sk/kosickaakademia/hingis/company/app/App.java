package sk.kosickaakademia.hingis.company.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = "sk.kosickaakademia.hingis.company.controller")
public class App {
    public static void main(String[] args) {

        System.out.println(args[0]);
        SpringApplication app = new SpringApplication(App.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", args[0]));
        app.run(args);
    }
}
