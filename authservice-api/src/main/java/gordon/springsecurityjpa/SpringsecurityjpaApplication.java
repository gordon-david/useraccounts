package gordon.springsecurityjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import gordon.springsecurityjpa.models.UserRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SpringsecurityjpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityjpaApplication.class, args);
	}

}
