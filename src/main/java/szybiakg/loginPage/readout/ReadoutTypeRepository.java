package szybiakg.loginPage.readout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ReadoutTypeRepository extends CrudRepository<ReadoutType, Integer> {
    Optional<ReadoutType> findBySymbol(String symbol);
    Optional<ReadoutType> findById(Integer Id);

}
