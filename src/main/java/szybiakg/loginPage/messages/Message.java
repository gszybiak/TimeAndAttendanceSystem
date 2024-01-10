package szybiakg.loginPage.messages;

import jakarta.persistence.*;
import lombok.Data;
import szybiakg.loginPage.employee.Employee;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "recipientId")
    private Employee recipient;
    @ManyToOne
    @JoinColumn(name = "senderId")
    private Employee sender;
    private LocalDateTime date;

}
