package co.edu.uco.kudos.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

    private static final String URL_FRONTEND = "http://localhost:5173";

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(peticiones -> peticiones
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(login -> login
                        .defaultSuccessUrl(URL_FRONTEND + "/", true)
                        .failureUrl(URL_FRONTEND + "/login?error"))

                .exceptionHandling(errores -> errores
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                PathPatternRequestMatcher.withDefaults().matcher("/api/**")))

                .logout(salida -> salida
                        .logoutUrl("/api/v1/autenticacion/salir")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))

                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
