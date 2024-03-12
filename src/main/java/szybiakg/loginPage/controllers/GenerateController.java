package szybiakg.loginPage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class GenerateController {

    private final ReadoutService readoutService;

    public GenerateController(ReadoutService readoutService) {
        this.readoutService = readoutService;
    }

    @RequestMapping("generateWorkPdf")
    public void generateWorkPdf(HttpServletResponse response) throws IOException {
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.PRESENCE.name(), null);
        PresencePdfGenerator generator = new PresencePdfGenerator();
        generator.generatePdf(presenceList, response);
    }

    @RequestMapping("generateWorkExcel")
    public void generateWorkExcel(HttpServletResponse response){
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.PRESENCE.name(), null);
        PresenceExcelGenerator generator = new PresenceExcelGenerator();
        generator.generateExcel(presenceList, response);
    }

    @RequestMapping("generateBreakPdf")
    public void generateBreakPdf(HttpServletResponse response) throws IOException {
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.BREAK.name(), null);
        BreakPdfGenerator generator = new BreakPdfGenerator();
        generator.generatePdf(presenceList, response);
    }

    @RequestMapping("generateBreakExcel")
    public void generateBreakExcel(HttpServletResponse response){
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.BREAK.name(), null);
        BreakExcelGenerator generator = new BreakExcelGenerator();
        generator.generateExcel(presenceList, response);
    }

    @RequestMapping("generateHolidayPdf")
    public void generateHolidayPdf(HttpServletResponse response) throws IOException {
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(null);
        HolidayPdfGenerator generator = new HolidayPdfGenerator();
        generator.generatePdf(listHolidays, response);
    }

    @RequestMapping("generateHolidayExcel")
    public void generateHolidayExcel(HttpServletResponse response){
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(null);
        HolidayExcelGenerator generator = new HolidayExcelGenerator();
        generator.generateExcel(listHolidays, response);
    }
}
