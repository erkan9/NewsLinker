package erkamber.repositories;

import erkamber.entities.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    Optional<Subscribe> findSubscribeBySubscriberIDAndSubscribedToID(int subscriberID, int subscribedToID);

    List<Subscribe> findSubscribeBySubscriberID(int subscriberID);

    List<Subscribe> findSubscribeBySubscribedToID(int subscribedToID);
}
