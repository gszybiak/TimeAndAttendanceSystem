package szybiakg.loginPage.generatorFile;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.PresenceType;
import szybiakg.loginPage.readout.ReadoutDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PdfGeneratorService {

    public static void generatePdf(List<ReadoutDto> presenceList, HttpServletResponse response, PresenceType type) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph header = new Paragraph(type.getDescription() + " List", font);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(type == PresenceType.H ? 5 : 3);

            if(type == PresenceType.H){
                table.addCell("Type holiday");
                table.addCell("Start ");
                table.addCell("End");
                table.addCell("Duration");
                table.addCell("Approval");

                for (ReadoutDto presence : presenceList) {
                    table.addCell(presence.getNameReadoutType());
                    table.addCell(presence.getStartTime());
                    table.addCell(presence.getEndTime());
                    table.addCell(presence.getDuration());
                    table.addCell(presence.getIsApproval() ? "1" : "0");
                }
            }
            else {
                table.addCell("Start Time");
                table.addCell("End Time");
                table.addCell("Duration");

                for (ReadoutDto presence : presenceList) {
                    table.addCell(presence.getStartTime());
                    table.addCell(presence.getEndTime());
                    table.addCell(presence.getDuration());
                }
            }

            document.add(table);
            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=generated" + type.getDescription() + "List.generatorFile");
            response.setContentLength(byteArrayOutputStream.size());

            OutputStream os = response.getOutputStream();
            byteArrayOutputStream.writeTo(os);
            os.flush();
            os.close();

        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during generate file PDF." + e.getMessage());
        }
    }
}
