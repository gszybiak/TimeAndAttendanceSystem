package szybiakg.loginPage.messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE (m.recipient.id = :recipientId AND m.sender.id = :senderId) OR (m.recipient.id = :senderId AND m.sender.id = :recipientId)")
    List<Message> findMessages(@Param("recipientId") Integer recipientId, @Param("senderId") Integer senderId);
}
