package unitec.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unitec.models.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}