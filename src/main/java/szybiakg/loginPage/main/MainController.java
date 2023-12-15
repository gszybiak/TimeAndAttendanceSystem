package szybiakg.loginPage.main;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

public class MainController {

    @RequestMapping("/main")
    String showMain(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);

        return "main";
    }
}
