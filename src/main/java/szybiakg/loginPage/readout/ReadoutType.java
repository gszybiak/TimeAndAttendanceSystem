package szybiakg.loginPage.readout;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ReadoutType")
class ReadoutType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String symbol;
    private String name;
}