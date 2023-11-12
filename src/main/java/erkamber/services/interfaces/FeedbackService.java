package erkamber.services.interfaces;

import erkamber.dtos.FeedbackDto;

import java.time.LocalDate;
import java.util.List;

public interface FeedbackService {

    int createFeedback(FeedbackDto feedbackDto);

    int deleteFeedbackByFeedbackID(int feedbackID);

    int deleteFeedbackByAuthorID(int authorID);

    int deleteFeedbacksBetweenDate(LocalDate startDate, LocalDate endDate);

    FeedbackDto getFeedbackById(int feedbackId);

    List<FeedbackDto> getAllFeedbacks();

    List<FeedbackDto> getFeedbacksByAuthorID(int authorID);

    List<FeedbackDto> getFeedbackBeforeCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackAfterCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackOnCreationDate(LocalDate creationDate);

    List<FeedbackDto> getFeedbackByContainingWord(String word);
}
