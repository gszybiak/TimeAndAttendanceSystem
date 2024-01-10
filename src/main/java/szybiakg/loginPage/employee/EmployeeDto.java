package szybiakg.loginPage.employee;

import lombok.Data;
import szybiakg.loginPage.user.UserDto;

@Data
public class EmployeeDto {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private UserDto userDto;
    private String userName;
    private String supervisorName;
    private String supervisorSurName;
    private String supervisorUserName;
    private String nfcCode;
    private boolean isValid;
}
