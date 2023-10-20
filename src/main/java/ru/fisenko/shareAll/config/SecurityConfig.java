package ru.fisenko.shareAll.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.fisenko.shareAll.services.PersonDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
////                .authorizeHttpRequests((authorize) -> authorize
////                        .anyRequest().authenticated()
////                )
//                .formLogin(form -> form.loginPage("/auth/login")
//                        .defaultSuccessUrl("/posts", true)
//                        .failureUrl("/auth/login?error"));
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/posts/**").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                )
                .formLogin(form -> form.loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/posts", true)
                .failureUrl("/auth/login?error")
                .permitAll());

        return http.build();
    }



    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }



}
