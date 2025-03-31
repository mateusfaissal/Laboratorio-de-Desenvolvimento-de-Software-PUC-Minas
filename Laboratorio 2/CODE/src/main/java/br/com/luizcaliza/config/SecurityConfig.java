package br.com.luizcaliza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.luizcaliza.services.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UsuarioService usuarioService;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
        .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) // Necessário para o H2 Console
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/registro/**", "/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao H2 Console
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/cliente/**").hasRole("CLIENTE")
            .requestMatchers("/banco/**").hasRole("BANCO")
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .successHandler((request, response, authentication) -> {
              // Redirecionamento personalizado com base no papel do usuário
              String redirectUrl = "/";
              if (authentication.getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin/dashboard";
              } else if (authentication.getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                redirectUrl = "/cliente/dashboard";
              } else if (authentication.getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_BANCO"))) {
                redirectUrl = "/banco/dashboard";
              }
              response.sendRedirect(redirectUrl);
            })
            .permitAll())
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .permitAll());

    return http.build();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(usuarioService)
        .passwordEncoder(passwordEncoder());
  }
}