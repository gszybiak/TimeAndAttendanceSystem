package szybiakg.loginPage.readout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szybiakg.loginPage.employee.Employee;

import java.util.List;
import java.util.Optional;

@Repository
interface ReadoutRepository extends CrudRepository<Readout, Integer> {
    Optional<Readout> findTopByEmployeeAndReadoutType_SymbolOrderByIdDesc(Employee employee, String readoutType);

    Optional<List<Readout>> findByEmployeeAndReadoutType_SymbolOrderByIdDesc(Employee employee, String readoutType);

    Optional<List<Readout>> findAllByEmployeeOrderByIdDesc(Employee employee);
}
