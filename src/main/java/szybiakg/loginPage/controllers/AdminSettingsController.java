package szybiakg.loginPage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import szybiakg.loginPage.employee.EmployeeDto;
import szybiakg.loginPage.employee.EmployeeService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminSettingsController {

    private final EmployeeService employeeService;

    public AdminSettingsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping("/settings")
    String showAdminSettings(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        List<EmployeeDto> employees = employeeService.findAllEmployeesExcludingGiven();
        model.addAttribute("employees", employees);
        return "admin/adminSettings";
    }

    @PostMapping("/setNfcCode")
    public String adminSetNfcCode(@RequestParam(name = "nfcCode") String nfcCode, @RequestParam(name = "employeeIdNfcCode") String employeeId, RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateAdminSetNfcCode(Integer.valueOf(employeeId), nfcCode);
            if(nfcCode == null || nfcCode.isEmpty()) {
                employeeService.updateAdminSetValid(Integer.valueOf(employeeId), true);
                redirectAttributes.addFlashAttribute("info", "Employee was set as invalid because You delete NFC Code");
                return "redirect:/admin/settings";
            }
            redirectAttributes.addFlashAttribute("info", "NFC code was set");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/settings";
    }

    @PostMapping("/setSupervisor")
    public String adminSetSupervisor(@RequestParam(name = "employeeIdSetSupervisor") String employeeId, @RequestParam(name = "supervisorId") String supervisorId, RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateAdminSetSupervisor(Integer.valueOf(employeeId), Integer.valueOf(supervisorId));
            redirectAttributes.addFlashAttribute("info", "Supervisor was set");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/settings";
    }

    @PostMapping("/setValid")
    public String setValid(@RequestParam(name = "validCheck", required = false) boolean validCheck, @RequestParam(name = "employeeIdSetValid") String employeeId, RedirectAttributes redirectAttributes) {
        try {
            boolean isValid = employeeService.updateAdminSetValid(Integer.valueOf(employeeId), false);
            redirectAttributes.addFlashAttribute("info", isValid ? "Employee was set as valid" : "Employee was set as invalid");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/settings";
    }
}

