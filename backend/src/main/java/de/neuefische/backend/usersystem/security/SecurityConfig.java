package de.neuefische.backend.usersystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final String ROLE_ALL = "ALL";
    private static final String ROLE_PURCHASE = "PURCHASE";
    private static final String ROLE_LEAD = "LEAD";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        requestAttributeHandler.setCsrfRequestAttributeName(null);

        httpSecurity
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(requestAttributeHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .httpBasic(basic -> basic.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(
                                        HttpStatus.UNAUTHORIZED.value(),
                                        HttpStatus.UNAUTHORIZED.getReasonPhrase()
                                )))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/userSystem/login").authenticated()
                        .requestMatchers("api/userSystem/logout").authenticated()
                        .requestMatchers("api/orderSystem/approve/**").hasAnyAuthority(ROLE_PURCHASE, ROLE_LEAD)
                        .requestMatchers("api/orderSystem/disapprove/**").hasAnyAuthority(ROLE_PURCHASE, ROLE_LEAD)
                        .requestMatchers("api/orderSystem/own").hasAuthority(ROLE_ALL)
                        .requestMatchers("api/orderSystem/own/**").hasAuthority(ROLE_ALL)
                        .requestMatchers(HttpMethod.GET, "api/orderSystem/{orderId}").hasAnyAuthority(ROLE_PURCHASE, ROLE_LEAD)
                        .requestMatchers("api/orderSystem/**").hasAuthority(ROLE_ALL)
                        .requestMatchers(HttpMethod.GET, "api/productSystem").hasAuthority(ROLE_ALL)
                        .requestMatchers(HttpMethod.POST, "api/productSystem").hasAuthority(ROLE_PURCHASE)
                        .requestMatchers("api/productSystem/**").hasAuthority(ROLE_PURCHASE)
                        .anyRequest().denyAll());
        return httpSecurity.build();
    }
}
