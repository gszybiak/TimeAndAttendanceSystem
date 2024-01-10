package szybiakg.loginPage.readout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szybiakg.loginPage.employee.Employee;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Readouts")
class Readout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isApproval;
    @ManyToOne
    @JoinColumn(name = "readoutTypeId")
    private ReadoutType readoutType;
    @Transient
    private Duration duration;
    @Transient
    private Boolean isLate;

    public Duration getDuration(){
        if(startTime == null || endTime == null)
            return Duration.ZERO;
        return Duration.between(startTime, endTime);
    }

    public Boolean getIsLate(){
        return startTime.getHour() > 8;
    }

}
