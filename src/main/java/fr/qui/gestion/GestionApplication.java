package fr.qui.gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(GestionApplication.class);
        application.run(args);
	}

}
