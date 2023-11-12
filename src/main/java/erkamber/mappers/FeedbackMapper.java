package erkamber.mappers;


import erkamber.dtos.FeedbackDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackDto mapFeedbackToFeedbackDto(Feedback feedback, UserDto userDto) {

        return new FeedbackDto(feedback.getFeedbackID(), userDto, feedback.getFeedbackContent(),
                feedback.getFeedbackCreationDate());
    }

    public Feedback mapFeedbackDtoToFeedback(FeedbackDto feedbackDto) {

        return new Feedback(feedbackDto.getFeedbackID(), feedbackDto.getAuthorDto().getUserID(),
                feedbackDto.getFeedbackContent(), feedbackDto.getFeedbackCreationDate());
    }
}
