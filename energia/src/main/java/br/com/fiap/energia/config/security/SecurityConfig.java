package br.com.fiap.energia.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private VerificarToken verificarToken;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/equipamentos").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/equipamentos").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/equipamentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/equipamentos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/equipamentos/{tipoEquipamento}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/equipamentos/{dataInicial}/{dataFinal}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/equipamentos/buscarPeloId/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/equipamentos/buscarPeloSensorId/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/usuarios").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/buscarPeloId/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/buscarPeloNome/{nome}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/listarPelaDataCadastro/{dataInicial}/{dataFinal}").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(verificarToken, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
