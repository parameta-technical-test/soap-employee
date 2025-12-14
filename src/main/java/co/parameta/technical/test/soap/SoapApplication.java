package co.parameta.technical.test.soap;

import co.parameta.technical.test.commons.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Main entry point for the SOAP application.
 * <p>
 * This class bootstraps the Spring Boot context for the SOAP layer,
 * enabling component scanning across the technical test packages
 * and importing shared configuration from the commons module.
 * </p>
 *
 * <p>
 * It exposes SOAP endpoints and integrates security, persistence,
 * and common utilities provided by the shared library.
 * </p>
 */
@SpringBootApplication
@ComponentScan(
		basePackages = "co.parameta.technical.test"
)
@Import({ApplicationConfig.class})
public class SoapApplication {

	/**
	 * Starts the SOAP Spring Boot application.
	 *
	 * @param args application startup arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SoapApplication.class, args);
	}

}
