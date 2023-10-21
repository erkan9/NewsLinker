package erkamber.services.interfaces;

import erkamber.dtos.FeedbackDto;
import erkamber.entities.Feedback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackService {

    int createFeedback(FeedbackDto feedbackDto);

    int deleteFeedbackByFeedbackID(int feedbackID);

    int deleteFeedbackByUserID(int userID);

    int deleteFeedbacksBetweenDate(LocalDate startDate, LocalDate endDate);

    List<FeedbackDto> getAllFeedbacks();

    List<FeedbackDto> getFeedbacksByAuthorID(int authorID);

    List<FeedbackDto> getFeedbackBeforeCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackAfterCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackOnCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackByContainingWord(String word);
}
