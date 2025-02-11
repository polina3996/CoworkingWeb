package coworking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "coworking")
@EntityScan(basePackages = "coworking.model")
public class CoworkingSpaceReservationApp {
    public static void main(String[] args) {
        SpringApplication.run(CoworkingSpaceReservationApp.class, args);
    }
}