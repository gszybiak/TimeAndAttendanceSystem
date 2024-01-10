package szybiakg.loginPage.readout;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayForm {
    private Integer holidayTypeId;
    private boolean usePeriod;
    private LocalDate singleDate;
    private LocalDate startDate;
    private LocalDate endDate;
}
