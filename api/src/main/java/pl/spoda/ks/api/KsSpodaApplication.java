package pl.spoda.ks.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "pl.spoda.ks.*")
//@EnableJpaRepositories(basePackages = {"pl.spoda.ks.database.*"})
@EntityScan(basePackages = {"pl.spoda.ks.database.*"})
public class KsSpodaApplication {
	public static void main(String[] args) {
		SpringApplication.run(KsSpodaApplication.class, args);
	}

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info( new Info()
						.title( "ks-spoda API" )
						.version( "1.0.0" )
						.description( "OPIS API" )
						.termsOfService( "http://swagger.io/terms/" )
						.license( new License()
								.name( "Apache 2.0" )
								.url( "http://springdoc.org" )
						) );
	}
}
