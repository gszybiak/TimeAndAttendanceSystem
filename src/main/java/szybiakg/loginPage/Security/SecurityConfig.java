package szybiakg.loginPage.Security;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login.loginPage("/login").permitAll()
                .successForwardUrl("/main")
                .failureUrl("/login?error=loginError"));
        http.logout(logout -> logout.logoutSuccessUrl("/logout").permitAll());
        http.authorizeHttpRequests(
                requests -> requests
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**","/addNewUser").permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf().disable();

        return http.build();
    }
}
