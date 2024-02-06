package szybiakg.loginPage.employee;

import jakarta.persistence.*;
import lombok.Data;
import szybiakg.loginPage.user.User;

@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String email;
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "supervisorId")
    private User supervisor;
}