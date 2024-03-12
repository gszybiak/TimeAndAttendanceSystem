package szybiakg.loginPage.generatorExcel;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.readout.ReadoutDto;

@Service
public class BreakExcelGenerator extends ExcelGenerator{

    @Override
    protected String getSheetName() {
        return "Break";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[]{"Start Time", "End Time", "Duration"};
    }

    @Override
    protected String[] getRowData(ReadoutDto presence) {
        return new String[]{
                presence.getStartTime(),
                presence.getEndTime(),
                presence.getDuration()
        };
    }
}