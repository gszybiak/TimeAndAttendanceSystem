package szybiakg.loginPage.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addNewUser")
    public String addUser(@RequestBody UserDto userDto, Model model) {
        userService.addUser(userDto);
        model.addAttribute("info", "User was added");
        return "login";
    }
}
