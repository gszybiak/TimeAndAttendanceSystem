package szybiakg.loginPage.employee;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.auth.AuthHelper;
import szybiakg.loginPage.user.User;
import szybiakg.loginPage.user.UserRepository;
import szybiakg.loginPage.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;
    private final EmployeeDtoMapper employeeDtoMapper;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository, EmployeeDtoMapper employeeDtoMapper) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.employeeDtoMapper = employeeDtoMapper;
    }

    public Optional<EmployeeDto> findAuthenticatedEmployee() {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        return employeeRepository.findByUserId(userId)
                .map(employeeDtoMapper::map);
    }

    public List<EmployeeDto> findAllEmployeesExcludingGiven(Integer employeeId) {
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        return employees.stream()
                .filter(employee -> !Objects.equals(employeeId, employee.getId()))
                .filter(employee -> !hasAdminRole(employee.getUser()))
                .map(employeeDtoMapper::map)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto> findAllSupervisors() {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        return employees.stream()
                .filter(employee -> !hasAdminRole(employee.getUser()))
                .filter(employee -> !employee.getUser().getId().equals(userId))
                .filter(employee -> employee.getSupervisor().getId().equals(userId))
                .map(employeeDtoMapper::map)
                .collect(Collectors.toList());
    }

    private boolean hasAdminRole(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "admin".equalsIgnoreCase(role.getName()));
    }

    public Optional<Employee> findAuthenticatedEmployeeByEntity() {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        return employeeRepository.findByUserId(userId);
    }

    public Optional<Employee> findEmployeeByUserId(Integer userId) {
        return employeeRepository.findByUserId(userId);
    }

    public Optional<Employee> findById(Integer employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public void updateEmployee(String name, String surname, String email){
        Employee employee = findAuthenticatedEmployeeByEntity().get();
        employee.setName(name);
        employee.setSurname(surname);
        employee.setEmail(email);
        employeeRepository.save(employee);
    }

    public void updateAdminSetNfcCode(Integer employeeId, String nfcCode){
        Employee employee = findById(employeeId).get();
        User user = employee.getUser();
        user.setNfcCode(nfcCode);
        userRepository.save(user);
    }

    public void updateAdminSetSupervisor(Integer employeeId, Integer supervisorId){
        Employee employee = findById(employeeId).get();
        if(supervisorId != null) {
            User supervisor = findById(supervisorId).get().getUser();
            employee.setSupervisor(supervisor);
        }
        else
            employee.setSupervisor(null);
        employeeRepository.save(employee);
    }

    public boolean updateAdminSetValid(Integer employeeId, boolean setDefaultInvalid){
        Employee employee = findById(employeeId).get();
        User user = employee.getUser();
        if(!setDefaultInvalid && (user.getNfcCode() == null || user.getNfcCode().isEmpty()))
            throw new RuntimeException("Employee has to have set NFC code.");
        if(employee.getSupervisor() == null)
            throw new RuntimeException("Employee has to have set Supervisor.");
        if(setDefaultInvalid)
            user.setValid(false);
        else
            user.setValid(!user.isValid());
        userRepository.save(user);

        return user.isValid();
    }
}
