package todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import todoapp.web.WebMvcConfiguration;

@SpringBootApplication
public class TodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}

	@Bean
	public WebMvcConfiguration todoappWebConfiguration() {
		return new WebMvcConfiguration();
	}
}
