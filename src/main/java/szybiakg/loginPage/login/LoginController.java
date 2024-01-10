package szybiakg.loginPage.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import szybiakg.loginPage.employee.EmployeeService;
import szybiakg.loginPage.readout.ReadoutService;
import szybiakg.loginPage.user.UserDto;
import szybiakg.loginPage.user.UserService;

import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    private UserService userService;
    private ReadoutService readoutService;

    public LoginController(UserService userService, ReadoutService readoutService) {
        this.userService = userService;
        this.readoutService = readoutService;
    }

    @RequestMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error){
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);
        return "login";
    }

    @PostMapping("/logWithoutAuthPresence")
    public String logWithoutAuthPresence(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
        boolean isPresence = false;
        Optional<UserDto> userByNfcCode = userService.findUserByNfcCode(nfcCode);
        if(userByNfcCode.isEmpty())
            redirectAttributes.addFlashAttribute("errorNFC", "NFC card not recognized");
        else {
            try {
                isPresence = readoutService.addPresence(userByNfcCode.get().getId());
                redirectAttributes.addFlashAttribute("infoNFC", isPresence ? "Work was started" : "Work was ended");
            } catch(Exception ex){
                redirectAttributes.addFlashAttribute("errorNFC", ex.getMessage());
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/logWithoutAuthBreak")
    public String logWithoutAuthSBreak(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
        boolean isBreak = false;
        Optional<UserDto> userByNfcCode = userService.findUserByNfcCode(nfcCode);
        if (userByNfcCode.isEmpty())
            redirectAttributes.addFlashAttribute("errorNFC", "NFC card not recognized");
        else {
            try {
                isBreak = readoutService.addBreak(userByNfcCode.get().getId());
                redirectAttributes.addFlashAttribute("infoNFC", isBreak ? "Break was started" : "Break was ended");
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("errorNFC", ex.getMessage());
            }
        }
        return "redirect:/login";
    }
}