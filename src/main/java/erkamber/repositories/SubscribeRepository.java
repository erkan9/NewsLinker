package erkamber.repositories;

import erkamber.entities.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    Optional<Subscribe> findSubscribeBySubscriberIDAndReporterID(int subscriberID, int reporterID);

    List<Subscribe> findSubscribeBySubscriberID(int subscriberID);

    List<Subscribe> findSubscribeByReporterID(int reporterID);
}
