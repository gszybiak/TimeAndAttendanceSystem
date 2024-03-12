package szybiakg.loginPage.generatorPdf;

import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.ReadoutDto;

import java.util.List;

@Service
public class BreakPdfGenerator extends PdfGenerator{

    @Override
    protected String getDocumentTitle() {
        return "Break List";
    }

    @Override
    protected PdfPTable createTable(List<ReadoutDto> presenceList) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        table.addCell("Start Time");
        table.addCell("End Time");
        table.addCell("Duration");

        for (ReadoutDto presence : presenceList) {
            table.addCell(presence.getStartTime());
            table.addCell(presence.getEndTime());
            table.addCell(presence.getDuration());
        }

        return table;
    }
}
