package szybiakg.loginPage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import szybiakg.loginPage.employee.EmployeeDto;
import szybiakg.loginPage.employee.EmployeeService;
import szybiakg.loginPage.messages.MessageDto;
import szybiakg.loginPage.messages.MessageForm;
import szybiakg.loginPage.messages.MessageService;
import szybiakg.loginPage.readout.*;
import szybiakg.loginPage.user.UserDto;
import szybiakg.loginPage.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ReadoutService readoutService;
    private final MessageService messageService;

    public MainController(UserService userService, ReadoutService readoutService, EmployeeService employeeService, MessageService messageService) {
        this.userService = userService;
        this.readoutService = readoutService;
        this.employeeService = employeeService;
        this.messageService = messageService;
    }

    @RequestMapping("/main")
    String showMain(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);

        readoutService.prepareMainModelData(model);
        return "main";
    }

    @PostMapping("/logPresence")
    public String logPresence(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
        boolean isPresence = false;
        Optional<UserDto> user = userService.findAuthenticatedUser();
        if (!user.get().getNfcCode().equals(nfcCode)) {
            redirectAttributes.addFlashAttribute("error", "You have to use your own NFC card to presence log");
            System.out.println(nfcCode + "źle odczytana karta");
        }
        else
        {
            try {
                isPresence = readoutService.addPresence(user.get().getId());
                redirectAttributes.addFlashAttribute("info", isPresence ? "Work was started" : "Work was ended");
            } catch(Exception ex){
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
            }
        }
        return "redirect:/main";
    }

    @PostMapping("/logBreak")
    public String logBreak(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
        boolean isBreak = false;
        Optional<UserDto> user = userService.findAuthenticatedUser();
        if (!user.get().getNfcCode().equals(nfcCode)) {
            redirectAttributes.addFlashAttribute("error", "You have to use your own NFC card to break log");
            System.out.println(nfcCode + "źle odczytana karta");
        }
        else
        {
            try {
                isBreak = readoutService.addBreak(user.get().getId());
                redirectAttributes.addFlashAttribute("info", isBreak ? "Break was started" : "Break was ended");
            } catch(Exception ex){
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
            }
        }
        return "redirect:/main";
    }

    @RequestMapping("/settings")
    String showSettings(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        EmployeeDto employee = employeeService.findAuthenticatedEmployee().get();
        UserDto user = userService.findAuthenticatedUser().get();
        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/updateEmployee")
    String updateEmployee(@RequestParam String name, @RequestParam String surname, @RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateEmployee(name, surname, email);
            redirectAttributes.addFlashAttribute("info", "Profile was update");
        } catch(Exception ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/settings";
        }

        return "redirect:/main";
    }

    @RequestMapping("/messages")
    String showMessages(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error, @RequestParam(required = false) Integer recipientId) {
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();
        List<MessageDto> messages = messageService.findMessagesByEmployeeId(employee.map(EmployeeDto::getId).orElse(null), recipientId);
        model.addAttribute("recipients", employeeService.findAllEmployeesExcludingGiven());
        model.addAttribute("messages", messages);
        MessageForm messageForm = new MessageForm();
        messageForm.setRecipientId(recipientId != null ? recipientId : (Integer) model.getAttribute("lastRecipientId"));
        model.addAttribute("messageForm", messageForm);
        model.addAttribute("recipientId", recipientId);
        model.addAttribute("lastRecipientId", recipientId != null ? recipientId : (Integer) model.getAttribute("lastRecipientId"));
        model.addAttribute("currentEmployeeId", employee.get().getId());

        return "messages";
    }

    @GetMapping("/loadTableData")
    @ResponseBody
    List<MessageDto> loadTableData(@RequestParam Integer recipientId) {
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();
        return messageService.findMessagesByEmployeeId(employee.map(EmployeeDto::getId).orElse(null), recipientId);
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<Integer> sendMessage(@RequestBody MessageForm messageForm) {
        Integer recipientId = messageForm.getRecipientId();
        String message = messageForm.getMessage();
        messageService.addMessage(message, recipientId);
        return ResponseEntity.ok(recipientId);
    }

    @RequestMapping("/holidays")
    String showHolidays(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);

        List<ReadoutTypeDto> listHolidayTypes = readoutService.getListTypeHolidays();
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(null);
        model.addAttribute("listHolidayTypes", listHolidayTypes);
        model.addAttribute("listHolidays", listHolidays);
        HolidayForm holidayForm = new HolidayForm();
        model.addAttribute("holidayForm", holidayForm);
        return "holidays";
    }

    @PostMapping("/addHoliday")
    public String addHoliday(@ModelAttribute HolidayForm holidayForm, RedirectAttributes redirectAttributes) {
        Integer holidayTypeId = holidayForm.getHolidayTypeId();
        boolean isUsePeriod = holidayForm.isUsePeriod();;
        LocalDate startDate = holidayForm.getStartDate();
        LocalDate endDate = holidayForm.getEndDate();
        LocalDate singleDate = holidayForm.getSingleDate();
        try {
            if (isUsePeriod && (startDate == null || endDate == null))
                throw new RuntimeException("Start date and end date have to be entered");
            else if(!isUsePeriod && singleDate == null)
                throw new RuntimeException("Single date can't be empty");
            else if(holidayTypeId == null)
                throw new RuntimeException("Type holiday can't be empty");

                readoutService.addHoliday(holidayTypeId, startDate != null ? startDate : singleDate, endDate != null ? endDate : singleDate, false);
                redirectAttributes.addFlashAttribute("info", "Holiday was added");
            } catch(Exception ex){
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
            }
        return "redirect:/holidays";
    }
}