package erkamber.repositories;

import erkamber.entities.Feedback;
import erkamber.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findFeedbackByAuthorID(int feedbackAuthorID);

    List<Feedback> findFeedbackByFeedbackCreationDateAfter(LocalDateTime feedbackCreationDate);

    List<Feedback> findFeedbackByFeedbackCreationDateBefore(LocalDateTime feedbackCreationDate);

    List<Feedback> findFeedbackByFeedbackCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Feedback> findFeedbackByFeedbackContentContaining(String feedbackContentContaining);
}
