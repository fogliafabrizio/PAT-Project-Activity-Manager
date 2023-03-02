package it.projectactivitymanager.PAT.config.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable() //Disattiva csrf verification
                // QUALI URL DA SECURE?
                // WHITE LIST - NO AUTH
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**", "/login", "/css/**", "/img/**") // Passiamo una lista di String
                .permitAll() // Permettiamo TUTTE
                .anyRequest() // Le altre richieste vanno AUTENTICATE
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login") // mapping URL della tua pagina di login personalizzata
                .defaultSuccessUrl("/") // URL di redirect dopo il login
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()

                .and()
                .sessionManagement() // Senza Stato in modo che siano tutte verificate
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and() //QUALE AUTHENTICATION PROVIDER USIAMO?
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //Vogliamo eseguire il filtro prima che chiami il filtro Usename e Password

        return http.build();
    }
}
