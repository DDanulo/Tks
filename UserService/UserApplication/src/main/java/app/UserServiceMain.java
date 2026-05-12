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
        "repository",
        "controllers"
})
public class UserServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceMain.class, args);
    }
}