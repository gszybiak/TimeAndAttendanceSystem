package szybiakg.loginPage.auth;

import lombok.Getter;
import szybiakg.loginPage.user.User;
import szybiakg.loginPage.user.UserRole;

import java.util.Set;

@Getter
public class UserCredentialsDto {
    private final String userName;
    private final String password;
    private final String role;

    public UserCredentialsDto(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public static UserCredentialsDto map(User user) {
        String userName = user.getUsername();
        String password = user.getPassword();
        String role = user.getRoles().stream()
                .findFirst()
                .map(UserRole::getName)
                .orElse(null);
        return new UserCredentialsDto(userName, password, role);
    }
}
