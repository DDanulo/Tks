package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "app",
        "service",
        "controller",
        "config",
        "security",
        "mappers",
        "aggregates",
        "client.repo",
        "repository"
})
public class Main {
    public static void main(String[] args) {
        System.out.print("Hello and welcome!");
        SpringApplication.run(Main.class, args);
    }
}