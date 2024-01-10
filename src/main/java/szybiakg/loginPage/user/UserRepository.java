package szybiakg.loginPage.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String login);
    Optional<User> findUserById(Integer id);
    Optional<User> findByNfcCode(String nfcCode);
}
