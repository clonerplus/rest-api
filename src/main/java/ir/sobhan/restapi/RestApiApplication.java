package ir.sobhan.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import ir.sobhan.restapi.service.individuals.AdminService;

@SpringBootApplication
@RestController
public class RestApiApplication {
	public static void main(String[] args) {

		SpringApplication.run(RestApiApplication.class, args);

		AdminService.createAdmin();
	}
}