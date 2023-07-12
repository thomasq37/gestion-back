package fr.qui.gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(GestionApplication.class);

        String env = System.getenv("ENVIRONMENT");
        if(env == null) {
        	application.setAdditionalProfiles("heroku");
        }
        else if (env != null && env.equalsIgnoreCase("local")) {
            application.setAdditionalProfiles("local");
        }
        application.run(args);
	}

}
