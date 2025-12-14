package co.parameta.technical.test.soap.configuration;

import co.parameta.technical.test.commons.util.exception.CustomAccessDeniedHandler;
import co.parameta.technical.test.commons.util.exception.CustomAuthenticationEntryPoint;
import co.parameta.technical.test.commons.util.helper.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for SOAP endpoints.
 * <p>
 * This configuration secures SOAP services using JWT-based authentication
 * and enforces stateless session management. It defines:
 * <ul>
 *   <li>JWT authentication filter integration</li>
 *   <li>Custom handlers for authentication and authorization errors</li>
 *   <li>Access rules for SOAP WSDL and service endpoints</li>
 * </ul>
 *
 * The security model follows REST-style stateless authentication
 * adapted for SOAP services.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Defines the main security filter chain.
     * <p>
     * Security rules:
     * <ul>
     *   <li>WSDL files are publicly accessible</li>
     *   <li>SOAP service endpoints require authentication</li>
     *   <li>All other requests are permitted</li>
     * </ul>
     *
     * JWT authentication is applied before the default
     * {@link UsernamePasswordAuthenticationFilter}.
     *
     * @param http {@link HttpSecurity} configuration object
     * @return configured {@link SecurityFilterChain}
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/ws/**.wsdl").permitAll()
                                .requestMatchers("/ws/**").authenticated()
                                .anyRequest().permitAll()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(handling ->
                        handling
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
