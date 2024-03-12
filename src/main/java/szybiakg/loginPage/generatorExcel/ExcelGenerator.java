package szybiakg.loginPage.generatorExcel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.ReadoutDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public abstract class ExcelGenerator {

    protected abstract  String getSheetName();
    protected abstract String[] getColumnHeaders();
    protected abstract String[] getRowData(ReadoutDto presence);

    public void generateExcel(List<ReadoutDto> presenceList, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(getSheetName());

            Row headerRow = sheet.createRow(0);
            String[] headers = getColumnHeaders();
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (ReadoutDto presence : presenceList) {
                Row row = sheet.createRow(rowNum++);
                String[] rowData = getRowData(presence);
                for (int i = 0; i < rowData.length; i++) {
                    row.createCell(i).setCellValue(rowData[i]);
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=generated_" + getSheetName() + "_List.xlsx");
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
