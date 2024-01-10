package szybiakg.loginPage.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String nfcCode;
    private boolean isValid;
}
