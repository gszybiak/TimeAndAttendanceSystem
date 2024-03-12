package szybiakg.loginPage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import szybiakg.loginPage.generatorExcel.BreakExcelGenerator;
import szybiakg.loginPage.generatorExcel.HolidayExcelGenerator;
import szybiakg.loginPage.generatorExcel.PresenceExcelGenerator;
import szybiakg.loginPage.generatorPdf.BreakPdfGenerator;
import szybiakg.loginPage.generatorPdf.HolidayPdfGenerator;
import szybiakg.loginPage.generatorPdf.PresencePdfGenerator;
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
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.PRESENCE.name(), subordinateId);
            PresencePdfGenerator generator = new PresencePdfGenerator();
            generator.generatePdf(presenceList, response);
        }
        return "redirect:/supervisor/presenceSubordinates";
    }

    @RequestMapping("generatePresenceSubordinatesExcel")
    public String generatePresenceSubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.PRESENCE.name(), subordinateId);
            PresenceExcelGenerator generator = new PresenceExcelGenerator();
            generator.generateExcel(presenceList, response);
        }
        return "redirect:/supervisor/presenceSubordinates";
    }

    @RequestMapping("generateBreakSubordinatesPdf")
    public String generateBreakSubordinatesPdf(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId) throws IOException {
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.BREAK.name(), subordinateId);
            BreakPdfGenerator generator = new BreakPdfGenerator();
            generator.generatePdf(presenceList, response);
        }
        return "redirect:/supervisor/breakSubordinates";
    }

    @RequestMapping("generateBreakSubordinatesExcel")
    public String generateBreakSubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.BREAK.name(), subordinateId);
            BreakExcelGenerator generator = new BreakExcelGenerator();
            generator.generateExcel(presenceList, response);
        }
        return "redirect:/supervisor/breakSubordinates";
    }

    @RequestMapping("generateHolidaySubordinatesPdf")
    public String generateHolidaySubordinatesPdf(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId) throws IOException {
        if(subordinateId != null) {
            List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(subordinateId);
            HolidayPdfGenerator generator = new HolidayPdfGenerator();
            generator.generatePdf(listHolidays, response);
        }
        return "redirect:supervisor/holidaySubordinates";
    }

    @RequestMapping("generateHolidaySubordinatesExcel")
    public String generateHolidaySubordinatesExcel(HttpServletResponse response, @RequestParam(required = false) Integer subordinateId){
        if(subordinateId != null) {
            List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(subordinateId);
            HolidayExcelGenerator generator = new HolidayExcelGenerator();
            generator.generateExcel(listHolidays, response);
        }
        return "redirect:supervisor/holidaySubordinates";
    }

    @RequestMapping("approveHolidays")
    public String approveHolidays(@RequestParam(required = false) Integer subordinateId, RedirectAttributes redirectAttributes){
        if(readoutService.approveHolidays(subordinateId))
            redirectAttributes.addFlashAttribute("info", "Holidays was approved");

        return "redirect:/supervisor/holidaySubordinates";
    }
}
