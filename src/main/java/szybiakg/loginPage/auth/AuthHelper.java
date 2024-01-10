package szybiakg.loginPage.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.user.User;
import szybiakg.loginPage.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthHelper {

    private static UserRepository userRepository;

    public AuthHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> users = (ArrayList<User>) userRepository.findAll();

        return users.stream()
                .filter(user -> user.getUsername().equals(authentication.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalAccessError("Couldn't find user with name: " + authentication.getName()));
    }

    public static Integer getAuthenticatedUserId() {
        User user = getAuthenticatedUser();
        return user.getId();
    }
}
