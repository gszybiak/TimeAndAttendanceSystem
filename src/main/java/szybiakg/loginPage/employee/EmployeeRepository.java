package szybiakg.loginPage.employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szybiakg.loginPage.user.User;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    Optional<Employee> findByUserId(Integer userId);
}
