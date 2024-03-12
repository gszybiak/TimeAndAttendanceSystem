package szybiakg.loginPage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login.loginPage("/login").permitAll()
                .successHandler(new LoginSuccessHandler())
                .failureUrl("/login?error=Incorrect+data"));
        http.logout(logout -> logout.logoutSuccessUrl("/login?info=User+was+logged+out").permitAll());
        http.authorizeHttpRequests(
                requests -> requests
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**","/user/add","/logWithoutAuthBreak","/logWithoutAuthPresence").permitAll()
                        .requestMatchers("/admin*").hasRole("ADMIN")
                        .requestMatchers("/supervisor*").hasRole("SUPERVISOR")
                        .anyRequest().authenticated()
        );

        http.csrf().disable();

        return http.build();
    }
}
