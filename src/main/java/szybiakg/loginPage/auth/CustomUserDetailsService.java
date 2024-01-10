package szybiakg.loginPage.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.user.UserRepository;
import szybiakg.loginPage.user.UserService;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userService.findCredentialsByLogin(login)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with login not found"));
    }

    private UserDetails createUserDetails(UserCredentialsDto credentials) {
        return User.builder()
                .username(credentials.getUserName())
                .password(credentials.getPassword())
                .roles(credentials.getRole() != null ? credentials.getRole() : "")
                .build();
    }
}
