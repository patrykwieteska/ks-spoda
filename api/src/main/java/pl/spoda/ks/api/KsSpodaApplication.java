package pl.spoda.ks.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "pl.spoda.ks, pl.spoda.ks.database, pl.spoda.ks.api, pl.spoda")
@EnableJpaRepositories(basePackages = {"pl.spoda.ks.database.repository"})
@EntityScan(basePackages = {"pl.spoda.ks.database.model"})
public class KsSpodaApplication {
	public static void main(String[] args) {
		SpringApplication.run(KsSpodaApplication.class, args);
	}
}
