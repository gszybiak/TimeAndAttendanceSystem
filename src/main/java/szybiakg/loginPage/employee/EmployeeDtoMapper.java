package szybiakg.loginPage.employee;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.user.UserService;

@Service
public class EmployeeDtoMapper {

    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    public EmployeeDtoMapper(UserService userService, EmployeeRepository employeeRepository) {
        this.userService = userService;
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDto map(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        userService.findAuthenticatedUser().ifPresent(employeeDto::setUserDto);
        employeeDto.setNfcCode(userService.findUserById(employee.getUser().getId()).get().getNfcCode());
        employeeDto.setValid(userService.findUserById(employee.getUser().getId()).get().isValid());
        employeeDto.setUserName(userService.findUserById(employee.getUser().getId()).get().getUsername());
        employeeDto.setName(employee.getName());
        employeeDto.setSurname(employee.getSurname());
        employeeDto.setEmail(employee.getEmail());
        if(employee.getSupervisor() != null)
            employeeRepository.findByUserId(employee.getSupervisor().getId()).ifPresent(supervisor -> {
                employeeDto.setSupervisorName(supervisor.getName());
                employeeDto.setSupervisorSurName(supervisor.getSurname());
                employeeDto.setSupervisorUserName(supervisor.getUser().getUsername());
            });

        return employeeDto;
    }
}
