package szybiakg.loginPage.generatorPdf;

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
public abstract class PdfGenerator {

    protected abstract String getDocumentTitle();
    protected abstract PdfPTable createTable(List<ReadoutDto> presenceList);

    public void generatePdf(List<ReadoutDto> presenceList, HttpServletResponse response) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph header = new Paragraph(getDocumentTitle(), font);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            PdfPTable table = createTable(presenceList);
            document.add(table);
            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=generated_" + getDocumentTitle().toLowerCase() + "_List.pdf");
            response.setContentLength(byteArrayOutputStream.size());

            try (OutputStream os = response.getOutputStream()) {
                byteArrayOutputStream.writeTo(os);
                os.flush();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during generate PDF file: " + e.getMessage());
        }
    }
}
