package erkamber.repositories;

import erkamber.entities.Feedback;
import erkamber.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findFeedbackByAuthorID(int feedbackAuthorID);

    List<Feedback> findFeedbackByFeedbackCreationDateAfter(LocalDateTime feedbackCreationDate);

    List<Feedback> findFeedbackByFeedbackCreationDateBefore(LocalDateTime feedbackCreationDate);

    List<Feedback> findFeedbackByFeedbackContentContaining(String feedbackContentContaining);
}
