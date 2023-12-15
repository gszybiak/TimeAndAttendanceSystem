package szybiakg.loginPage.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error){
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
