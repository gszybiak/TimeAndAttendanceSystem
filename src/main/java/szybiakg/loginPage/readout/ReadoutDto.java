package szybiakg.loginPage.readout;

import lombok.Data;

@Data
public class ReadoutDto {
    private Integer id;
    private Integer employeeId;
    private String startTime;
    private String endTime;
    private Integer readoutTypeId;
    private String duration;
    private Integer durationSecond;
    private Boolean isLate;
    private Boolean isApproval;
    private String nameReadoutType;
}
