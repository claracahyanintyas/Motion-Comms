package org.individualproject.semester3project.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAuthenticationFilter authenticationFilter;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins; // comma-separated

    public SpringSecurityConfig(UserDetailsService userDetailsService,
                                JwtAuthenticationEntryPoint authenticationEntryPoint,
                                JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String commissionPath = "/api/commissions/**";
        String commissionOrderPath = "/api/commission-orders/**";
        String userPath = "/api/users/**";
        String payoutMethodPath = "/api/payout-method/**";
        String transactionPath = "/api/transactions/**";

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // Disable session creation entirely
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/api/users/register").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, userPath).permitAll();
                    authorize.requestMatchers(HttpMethod.PUT, userPath).authenticated();
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, commissionPath).permitAll();
                    authorize.requestMatchers(HttpMethod.POST, commissionPath).authenticated();
                    authorize.requestMatchers(HttpMethod.DELETE, commissionPath).authenticated();
                    authorize.requestMatchers(HttpMethod.PUT, commissionPath).authenticated();
                    authorize.requestMatchers(HttpMethod.POST, commissionOrderPath).authenticated();
                    authorize.requestMatchers(HttpMethod.GET, commissionOrderPath).authenticated();
                    authorize.requestMatchers(HttpMethod.PATCH, commissionOrderPath).authenticated();
                    authorize.requestMatchers(HttpMethod.POST, payoutMethodPath).authenticated();
                    authorize.requestMatchers(HttpMethod.DELETE, payoutMethodPath).authenticated();
                    authorize.requestMatchers(HttpMethod.PUT, payoutMethodPath).authenticated();
                    authorize.requestMatchers(HttpMethod.GET, payoutMethodPath).permitAll();
                    authorize.requestMatchers(HttpMethod.PATCH, transactionPath).authenticated();
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint)
                )
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Prevent SecurityContext from creating a session implicitly
                .securityContext(securityContext ->
                        securityContext.requireExplicitSave(false)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}