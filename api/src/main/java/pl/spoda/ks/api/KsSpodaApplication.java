package pl.spoda.ks.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "pl.spoda.ks, pl.spoda.ks.database, pl.spoda.ks.api, pl.spoda")
@EnableJpaRepositories(basePackages = {"pl.spoda.ks.database.repository"})
@EntityScan(basePackages = {"pl.spoda.ks.database.entity"})
@EnableFeignClients
public class KsSpodaApplication {
	public static void main(String[] args) {
		SpringApplication.run(KsSpodaApplication.class, args);
	}

	/* TODO LISTA ZMIAN DO EURO
	//	BE
		1. Gdy euro pobieramy inną listę zespołów (Tylko uczestnicy ME2024). Zespoły w kolejce mogą się powtarzać.
		2. Po dodaniu/ aktualziacji meczu należy zaktualizować rezultat w EuroService
		3. Po usunięciu meczu należy zaktualizować rezultat w euroMeczu - zresetować (EuroService);
		4. Po zakończeniu meczu należy ustawić flagę euroMeczu na finished (EuroService)
		5. Zastanowić się co w przypadku zmiany nazwy gracza - jak to rozpropagować do EuroService? - funkcjonalnośc
		opcjonalna
	//	FE
		1. Dostosować request createSeason + dodać wyświetlanie logo sezonu i nazwy



	 */
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
