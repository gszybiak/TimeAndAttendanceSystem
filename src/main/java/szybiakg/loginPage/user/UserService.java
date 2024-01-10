package szybiakg.loginPage.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.auth.AuthHelper;
import szybiakg.loginPage.auth.UserCredentialsDto;
import szybiakg.loginPage.employee.Employee;
import szybiakg.loginPage.employee.EmployeeRepository;
import szybiakg.loginPage.employee.EmployeeService;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public Optional<UserCredentialsDto> findCredentialsByLogin(String login) {
        return userRepository.findByUsername(login)
                .map(UserCredentialsDto::map);
    }

    public Optional<UserDto> findAuthenticatedUser() {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        return userRepository.findById(userId)
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setPassword(user.getPassword());
                    userDto.setNfcCode(user.getNfcCode());
                    return userDto;
                });
    }

    public Optional<UserDto> findUserByNfcCode(String nfcCode) {
        return userRepository.findByNfcCode(nfcCode)
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setPassword(user.getPassword());
                    userDto.setNfcCode(user.getNfcCode());
                    return userDto;
                });
    }

    public Optional<UserDto> findUserById(Integer id) {
        return userRepository.findUserById(id)
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setPassword(user.getPassword());
                    userDto.setNfcCode(user.getNfcCode());
                    userDto.setValid(user.isValid());
                    return userDto;
                });
    }

    public Optional<User> findAuthenticatedUserByEntity() {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        return userRepository.findById(userId);
    }

    public void addUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(userDto.getPassword());

        user.setPassword(password);
        User savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setName(userDto.getName());
        employee.setSurname(userDto.getSurname());
        employee.setEmail(userDto.getEmail());
        employeeRepository.save(employee);
    }
}
