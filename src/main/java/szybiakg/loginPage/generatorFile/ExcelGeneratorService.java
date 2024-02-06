package szybiakg.loginPage.generatorFile;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import szybiakg.loginPage.readout.PresenceType;
import szybiakg.loginPage.readout.ReadoutDto;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelGeneratorService {

    public static void generateExcel(List<ReadoutDto> presenceList, HttpServletResponse response,  PresenceType type){
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(type.getDescription() + " List");

            if(type == PresenceType.H) {
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Type holiday");
                headerRow.createCell(1).setCellValue("Start");
                headerRow.createCell(2).setCellValue("End");
                headerRow.createCell(2).setCellValue("Duration");
                headerRow.createCell(2).setCellValue("Approval");

                int rowNum = 1;
                for (ReadoutDto presence : presenceList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(presence.getNameReadoutType());
                    row.createCell(1).setCellValue(presence.getStartTime());
                    row.createCell(2).setCellValue(presence.getEndTime());
                    row.createCell(3).setCellValue(presence.getDuration());
                    row.createCell(4).setCellValue(presence.getIsApproval() ? "1" : "0");
                }
            }else {
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Start Time");
                headerRow.createCell(1).setCellValue("End Time");
                headerRow.createCell(2).setCellValue("Duration");

                int rowNum = 1;
                for (ReadoutDto presence : presenceList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(presence.getStartTime());
                    row.createCell(1).setCellValue(presence.getEndTime());
                    row.createCell(2).setCellValue(presence.getDuration());
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=generated" + type.getDescription() + "List.xlsx");
            response.setContentLength(byteArrayOutputStream.size());

            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during generate file PDF." + e.getMessage());
        }
    }
}

