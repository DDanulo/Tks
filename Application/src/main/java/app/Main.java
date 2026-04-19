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
        SpringApplication.run(Main.class, args);
    }
}