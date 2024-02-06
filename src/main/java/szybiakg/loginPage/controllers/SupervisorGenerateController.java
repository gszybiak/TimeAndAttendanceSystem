package szybiakg.loginPage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import szybiakg.loginPage.generatorFile.ExcelGeneratorService;
import szybiakg.loginPage.generatorFile.PdfGeneratorService;
import szybiakg.loginPage.readout.PresenceType;
import szybiakg.loginPage.readout.ReadoutDto;
import szybiakg.loginPage.readout.ReadoutService;

import java.io.IOException;
import java.util.List;

@Controller
public class SupervisorGenerateController {

    private final ReadoutService readoutService;

    public SupervisorGenerateController(ReadoutService readoutService) {
        this.readoutService = readoutService;
    }

    @RequestMapping("generatePresenceSubordinatesPdf")
    public String generatePresenceSubordinatesPdf(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId) throws IOException {
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.P.name(), subordinateId);
            PdfGeneratorService.generatePdf(presenceList, response, PresenceType.P);
        }
        return "redirect:/supervisorPresenceSubordinates";
    }

    @RequestMapping("generatePresenceSubordinatesExcel")
    public String generatePresenceSubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.P.name(), subordinateId);
            ExcelGeneratorService.generateExcel(presenceList, response, PresenceType.P);
        }
        return "redirect:/supervisorPresenceSubordinates";
    }

    @RequestMapping("generateBreakSubordinatesPdf")
    public String generateBreakSubordinatesPdf(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId) throws IOException {
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.B.name(), subordinateId);
            PdfGeneratorService.generatePdf(presenceList, response, PresenceType.B);
        }
        return "redirect:/supervisorBreakSubordinates";
    }

    @RequestMapping("generateBreakSubordinatesExcel")
    public String generateBreakSubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.B.name(), subordinateId);
            ExcelGeneratorService.generateExcel(presenceList, response, PresenceType.B);
        }
        return "redirect:/supervisorBreakSubordinates";
    }

    @RequestMapping("generateHolidaySubordinatesPdf")
    public String generateHolidaySubordinatesPdf(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId) throws IOException {
        if(subordinateId != null) {
            List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(subordinateId);
            PdfGeneratorService.generatePdf(listHolidays, response, PresenceType.H);
        }
        return "redirect:supervisorHolidaySubordinates";
    }

    @RequestMapping("generateHolidaySubordinatesExcel")
    public String generateHolidaySubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(subordinateId);
            ExcelGeneratorService.generateExcel(listHolidays, response, PresenceType.H);
        }
        return "redirect:supervisorHolidaySubordinates";
    }

    @RequestMapping("approveHolidays")
    public String approveHolidays(@RequestParam(required = false) Integer subordinateId, RedirectAttributes redirectAttributes){
        if(readoutService.approveHolidays(subordinateId))
            redirectAttributes.addFlashAttribute("info", "Holidays was approved");

        return "redirect:/supervisorHolidaySubordinates";
    }
}
