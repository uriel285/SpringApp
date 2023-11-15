package ar.com.gm.web;

import ar.com.gm.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    
    
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests((authz) ->
                authz
                    .requestMatchers("/editar/**", "/agregar/**", "/eliminar").hasRole("ADMIN")
                    .requestMatchers("/").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/webjars/bootstrap/5.3.2/css/bootstrap.min.css").permitAll()
                    .anyRequest().authenticated()
            );
            http
                    .formLogin(
                            form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                    );
            http
                    .httpBasic(withDefaults());
            http
                    .exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler()));

        return http.build();
    }
    
    //@Bean
    public void addViewControllers(ViewControllerRegistry registro) {
        registro.addViewController("/").setViewName("index");
        registro.addViewController("/login");
        registro.addViewController("/errores/403").setViewName("/errores/403");
    }
    
    @Bean
AccessDeniedHandler customAccessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
        // custom Handling here
        response.sendRedirect("/errores/403");
    };
}


}
