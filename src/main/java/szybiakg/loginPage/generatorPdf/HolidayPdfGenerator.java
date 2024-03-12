package szybiakg.loginPage.generatorPdf;

import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.PresenceType;
import szybiakg.loginPage.readout.ReadoutDto;

import java.util.List;

@Service
public class HolidayPdfGenerator extends PdfGenerator{

    @Override
    protected String getDocumentTitle() {
        return "Holiday List";
    }

    @Override
    protected PdfPTable createTable(List<ReadoutDto> presenceList) {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        table.addCell("Type holiday");
        table.addCell("Start");
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

        return table;
    }
}
