package szybiakg.loginPage.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.auth.AuthHelper;
import szybiakg.loginPage.auth.UserCredentialsDto;
import szybiakg.loginPage.employee.Employee;
import szybiakg.loginPage.employee.EmployeeRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        return userRepository.findById(id)
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

    public void add(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());

        String password = bCryptPasswordEncoder.encode(userDto.getPassword());
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
