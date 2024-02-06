package szybiakg.loginPage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import szybiakg.loginPage.employee.EmployeeDto;
import szybiakg.loginPage.employee.EmployeeService;
import szybiakg.loginPage.generatorFile.ExcelGeneratorService;
import szybiakg.loginPage.generatorFile.PdfGeneratorService;
import szybiakg.loginPage.messages.MessageDto;
import szybiakg.loginPage.messages.MessageForm;
import szybiakg.loginPage.messages.MessageService;
import szybiakg.loginPage.readout.*;
import szybiakg.loginPage.user.UserDto;
import szybiakg.loginPage.user.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class SupervisorMainController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ReadoutService readoutService;
    private final MessageService messageService;

    public SupervisorMainController(UserService userService, ReadoutService readoutService, EmployeeService employeeService, MessageService messageService) {
        this.userService = userService;
        this.readoutService = readoutService;
        this.employeeService = employeeService;
        this.messageService = messageService;
    }

    @RequestMapping("/supervisorMain")
    String showSupervisorMain(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);

        Optional<UserDto> userDto = userService.findAuthenticatedUser();
        model.addAttribute("break", readoutService.getIsBreak(userDto.get().getId()) ? "Log end brake" : "Log start brake");
        model.addAttribute("presence", readoutService.getIsPresence(userDto.get().getId()) ? "Log end presence" : "Log start presence");

        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.P.name(), null);
        List<ReadoutDto> breakList = readoutService.loadReadoutList(PresenceType.B.name(), null);
        model.addAttribute("presenceList", presenceList);
        model.addAttribute("breakList", breakList);
        model.addAttribute("workedHours", readoutService.getSumTime(presenceList));
        model.addAttribute("breakHours", readoutService.getSumTime(breakList));
        model.addAttribute("quanityLates", readoutService.getQuantityLates(presenceList));
        return "supervisor/supervisorMain";
    }

    @PostMapping("/supervisorLogPresence")
    public String supervisorLogPresence(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
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
        return "redirect:/supervisorMain";
    }

    @PostMapping("/supervisorLogBreak")
    public String supervisorLogBreak(@RequestParam String nfcCode, RedirectAttributes redirectAttributes) {
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
        return "redirect:/supervisorMain";
    }
    @RequestMapping("/supervisorSettings")
    String supervisorShowSettings(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
        EmployeeDto employee = employeeService.findAuthenticatedEmployee().get();
        UserDto user = userService.findAuthenticatedUser().get();
        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        return "supervisor/supervisorSettings";
    }

    @RequestMapping("/supervisorMessages")
    String supervisorShowMessages(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error, @RequestParam(required = false) Integer recipientId) {
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();
        List<MessageDto> messages = messageService.findMessagesByEmployeeId(employee.map(EmployeeDto::getId).orElse(null), recipientId);
        List<EmployeeDto> recipients = employeeService.findAllEmployeesExcludingGiven(employee.map(EmployeeDto::getId).orElse(null));
        model.addAttribute("recipients", recipients);
        model.addAttribute("messages", messages);
        MessageForm messageForm = new MessageForm();
        messageForm.setRecipientId(recipientId != null ? recipientId : (Integer) model.getAttribute("lastRecipientId"));
        model.addAttribute("messageForm", messageForm);
        model.addAttribute("recipientId", recipientId);
        model.addAttribute("lastRecipientId", recipientId != null ? recipientId : (Integer) model.getAttribute("lastRecipientId"));
        model.addAttribute("currentEmployeeId", employee.get().getId());

        return "supervisor/supervisorMessages";
    }

    @RequestMapping("/supervisorPresenceSubordinates")
    String supervisorPresenceSubordinates(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error, @RequestParam(required = false) Integer subordinateId) {
        List<EmployeeDto> subordinates = employeeService.findAllSupervisors();
        model.addAttribute("subordinates", subordinates);
        List<ReadoutDto> presenceSubordinateList = readoutService.loadReadoutList(PresenceType.P.name(), subordinateId);
        model.addAttribute("presenceSubordinateList", presenceSubordinateList);
        model.addAttribute("subordinateId", subordinateId);
        return "supervisor/supervisorPresenceSubordinates";
    }

    @RequestMapping("/supervisorBreakSubordinates")
    String supervisorBreakSubordinates(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error, @RequestParam(required = false) Integer subordinateId) {
        List<EmployeeDto> subordinates = employeeService.findAllSupervisors();
        model.addAttribute("subordinates", subordinates);
        List<ReadoutDto> breakSubordinateList = readoutService.loadReadoutList(PresenceType.B.name(), subordinateId);
        model.addAttribute("brakeSubordinateList", breakSubordinateList);
        model.addAttribute("subordinateId", subordinateId);
        return "supervisor/supervisorBreakSubordinates";
    }

    @GetMapping("/supervisorLoadTablePresenceSubordinates")
    @ResponseBody
    List<ReadoutDto> supervisorLoadTablePresenceSubordinates(@RequestParam Integer subordinateId) {
        return readoutService.loadReadoutList(PresenceType.P.name(), subordinateId);
    }

    @GetMapping("/supervisorLoadTableBreakSubordinates")
    @ResponseBody
    List<ReadoutDto> supervisorLoadTableBreakSubordinates(@RequestParam Integer subordinateId) {
        return readoutService.loadReadoutList(PresenceType.B.name(), subordinateId);
    }

    @RequestMapping("supervisorHolidaySubordinates")
    String supervisorHolidaySubordinates(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error, @RequestParam(required = false) Integer subordinateId) {
        if (!StringUtils.isEmpty(info))
            model.addAttribute("info", info);
        if (!StringUtils.isEmpty(error))
            model.addAttribute("error", error);

        List<EmployeeDto> subordinates = employeeService.findAllSupervisors();
        model.addAttribute("subordinates", subordinates);
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(subordinateId);
        model.addAttribute("listHolidays", listHolidays);
        return "supervisor/supervisorHolidaySubordinates";
    }

    @GetMapping("/supervisorLoadTableHolidaySubordinates")
    @ResponseBody
    List<ReadoutDto> supervisorLoadTableHolidaySubordinates(@RequestParam Integer subordinateId) {
        return readoutService.loadHolidaysList(subordinateId);
    }

    @GetMapping("/supervisorLoadTableData")
    @ResponseBody
    List<MessageDto> supervisorLoadTableData(@RequestParam Integer recipientId) {
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();
        return messageService.findMessagesByEmployeeId(employee.map(EmployeeDto::getId).orElse(null), recipientId);
    }

    @PostMapping("/supervisorSend")
    @ResponseBody
    public ResponseEntity<Integer> supervisorSendMessage(@RequestBody MessageForm messageForm) {
        Integer recipientId = messageForm.getRecipientId();
        String message = messageForm.getMessage();
        messageService.addMessage(message, recipientId);
        return ResponseEntity.ok(recipientId);
    }

    @RequestMapping("supervisorHolidays")
    String supervisorShowHolidays(Model model, @RequestParam(required = false) String info, @RequestParam(required = false) String error) {
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
        return "supervisor/supervisorHolidays";
    }

    @PostMapping("/supervisorAddHoliday")
    public String supervisorAddHoliday(@ModelAttribute HolidayForm holidayForm, RedirectAttributes redirectAttributes) {
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

            readoutService.addHoliday(holidayTypeId, startDate != null ? startDate : singleDate, endDate != null ? endDate : singleDate, true);
            redirectAttributes.addFlashAttribute("info", "Holiday was added");
        } catch(Exception ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/supervisorHolidays";
    }
}
