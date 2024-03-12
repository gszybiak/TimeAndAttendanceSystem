package szybiakg.loginPage.generatorExcel;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.ReadoutDto;

@Service
public class HolidayExcelGenerator extends ExcelGenerator {

    @Override
    protected String getSheetName() {
        return "Holiday";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[]{"Type holiday", "Start", "End", "Duration", "Approval"};
    }

    @Override
    protected String[] getRowData(ReadoutDto presence) {
        return new String[]{
                presence.getNameReadoutType(),
                presence.getStartTime(),
                presence.getEndTime(),
                presence.getDuration(),
                presence.getIsApproval() ? "1" : "0"
        };
    }
}
