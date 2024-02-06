package szybiakg.loginPage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import szybiakg.loginPage.generatorFile.ExcelGeneratorService;
import szybiakg.loginPage.generatorFile.PdfGeneratorService;
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
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.P.name(), null);
        PdfGeneratorService.generatePdf(presenceList, response, PresenceType.P);
    }

    @RequestMapping("generateWorkExcel")
    public void generateWorkExcel(HttpServletResponse response){
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.P.name(), null);
        ExcelGeneratorService.generateExcel(presenceList, response, PresenceType.P);
    }

    @RequestMapping("generateBreakPdf")
    public void generateBreakPdf(HttpServletResponse response) throws IOException {
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.B.name(), null);
        PdfGeneratorService.generatePdf(presenceList, response, PresenceType.B);
    }

    @RequestMapping("generateBreakExcel")
    public void generateBreakExcel(HttpServletResponse response){
        List<ReadoutDto> presenceList = readoutService.loadReadoutList(PresenceType.B.name(), null);
        ExcelGeneratorService.generateExcel(presenceList, response, PresenceType.B);
    }

    @RequestMapping("generateHolidayPdf")
    public void generateHolidayPdf(HttpServletResponse response) throws IOException {
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(null);
        PdfGeneratorService.generatePdf(listHolidays, response, PresenceType.H);
    }

    @RequestMapping("generateHolidayExcel")
    public void generateHolidayExcel(HttpServletResponse response){
        List<ReadoutDto> listHolidays = readoutService.loadHolidaysList(null);
        ExcelGeneratorService.generateExcel(listHolidays, response, PresenceType.H);
    }
}
