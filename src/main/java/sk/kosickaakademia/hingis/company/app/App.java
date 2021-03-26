package sk.kosickaakademia.hingis.company.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages = "sk.kosickaakademia.hingis.company.controller")
public class App {
    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

    }
}
