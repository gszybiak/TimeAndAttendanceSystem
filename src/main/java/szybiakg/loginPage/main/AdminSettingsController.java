package szybiakg.loginPage.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import szybiakg.loginPage.employee.EmployeeDto;
import szybiakg.loginPage.employee.EmployeeRepository;
import szybiakg.loginPage.employee.EmployeeService;
import szybiakg.loginPage.user.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminSettingsController {

    private final EmployeeService employeeService;
    private final UserService userService;

    public AdminSettingsController(EmployeeService employeeService, UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
    }

    @RequestMapping("/adminSettings")
    String showAdminSettings(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();

        List<EmployeeDto> employees = employeeService.findAllEmployeesExcludingGiven(employee.map(EmployeeDto::getId).orElse(null));
        model.addAttribute("employees", employees);
        return "admin/adminSettings";
    }

    @PostMapping("/adminSetNfcCode")
    public String adminSetNfcCode(@RequestParam(name = "nfcCode") String nfcCode, @RequestParam(name = "employeeId1") String employeeId, RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateAdminSetNfcCode(Integer.valueOf(employeeId), nfcCode);
            if(nfcCode == null || nfcCode.isEmpty()) {
                employeeService.updateAdminSetValid(Integer.valueOf(employeeId), true);
                redirectAttributes.addFlashAttribute("info", "Employee was set as invalid because You delete NFC Code");
                return "redirect:/adminSettings";
            }
            redirectAttributes.addFlashAttribute("info", "NFC code was set");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/adminSettings";
    }

    @PostMapping("/adminSetSupervisor")
    public String adminSetSupervisor(@RequestParam(name = "employeeId3") String employeeId, @RequestParam(name = "supervisorId") String supervisorId, RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateAdminSetSupervisor(Integer.valueOf(employeeId), Integer.valueOf(supervisorId));
            redirectAttributes.addFlashAttribute("info", "Supervisor was set");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/adminSettings";
    }

    @PostMapping("/adminSetValid")
    public String setValid(@RequestParam(name = "validCheck", required = false) boolean validCheck, @RequestParam(name = "employeeId2") String employeeId, RedirectAttributes redirectAttributes) {
        try {
            boolean isValid = employeeService.updateAdminSetValid(Integer.valueOf(employeeId), false);
            redirectAttributes.addFlashAttribute("info", isValid ? "Employee was set as valid" : "Employee was set as invalid");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/adminSettings";
    }
}

