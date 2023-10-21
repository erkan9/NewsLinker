package erkamber.services.implementations;

import erkamber.dtos.FeedbackDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Feedback;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.FeedbackMapper;
import erkamber.repositories.FeedbackRepository;
import erkamber.services.interfaces.FeedbackService;
import erkamber.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final UserService userService;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.userService = userService;
    }

    /**
     * Creates a new feedback based on the provided FeedbackDto object. The FeedbackDto is
     * first mapped to a Feedback entity using the feedbackMapper. The creation date of the
     * feedback is set to the current date and time. The new feedback entity is then saved
     * to the feedbackRepository.
     *
     * @param feedbackDto The Data Transfer Object (DTO) containing the information for
     *                    creating the feedback.
     * @return The unique identifier (ID) of the newly created feedback.
     * @see FeedbackDto
     * @see Feedback
     * @see FeedbackMapper
     * @see FeedbackRepository
     */
    @Override
    public int createFeedback(FeedbackDto feedbackDto) {

        Feedback newFeedback = feedbackMapper.mapFeedbackDtoToFeedback(feedbackDto);

        newFeedback.setFeedbackCreationDate(LocalDateTime.now());

        feedbackRepository.save(newFeedback);

        return newFeedback.getFeedbackID();
    }

    /**
     * Deletes the feedback with the specified unique identifier (ID). First, the method
     * attempts to find the feedback entity by its ID using the feedbackRepository.
     * If the feedback is found, it is deleted from the data source. If the feedback
     * with the given ID is not found, a ResourceNotFoundException is thrown indicating
     * that the feedback does not exist.
     *
     * @param feedbackID The unique identifier (ID) of the feedback to be deleted.
     * @return The unique identifier (ID) of the deleted feedback.
     * @throws ResourceNotFoundException If the feedback with the specified ID does not exist.
     * @see Feedback
     * @see FeedbackRepository
     * @see ResourceNotFoundException
     */
    @Override
    public int deleteFeedbackByFeedbackID(int feedbackID) {

        Optional<Feedback> searchedFeedback = feedbackRepository.findById(feedbackID);

        Feedback searchedFeedbackObject = searchedFeedback.orElseThrow(() ->
                new ResourceNotFoundException("Feedback not Found: " + feedbackID, "Feedback"));

        feedbackRepository.delete(searchedFeedbackObject);

        return searchedFeedbackObject.getFeedbackID();
    }

    /**
     * Deletes all feedback entries associated with the specified author's ID.
     *
     * @param authorID The unique identifier (ID) of the author whose feedback entries
     *                 need to be deleted.
     * @return The number of feedback entries deleted.
     * @see Feedback
     * @see FeedbackRepository
     */
    @Override
    public int deleteFeedbackByAuthorID(int authorID) {

        List<Feedback> searchedFeedback = feedbackRepository.findFeedbackByAuthorID(authorID);

        feedbackRepository.deleteAll(searchedFeedback);

        return searchedFeedback.size();
    }

    /**
     * Deletes all feedback entries created between the specified start and end dates,
     * inclusive.
     *
     * @param startDate The start date (inclusive) for deleting feedback entries.
     * @param endDate   The end date (inclusive) for deleting feedback entries.
     * @return The number of feedback entries deleted.
     * @see Feedback
     * @see FeedbackRepository
     */
    @Override
    public int deleteFeedbacksBetweenDate(LocalDate startDate, LocalDate endDate) {

        List<Feedback> listOfFeedbacksCreatedBetween =
                feedbackRepository.findFeedbackByFeedbackCreationDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay());

        feedbackRepository.deleteAll(listOfFeedbacksCreatedBetween);

        return listOfFeedbacksCreatedBetween.size();
    }

    /**
     * Retrieves a list of all feedback entries from the data source and maps them to
     * their corresponding Data Transfer Objects (DTOs).
     *
     * @return A list of FeedbackDto objects representing all feedback entries.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getAllFeedbacks() {

        List<Feedback> listOfAllFeedbacks = feedbackRepository.findAll();

        return mapFeedbackListToFeedbackDto(listOfAllFeedbacks);
    }

    /**
     * Retrieves a list of feedback entries created by the specified author's ID and maps them
     * to their corresponding Data Transfer Objects (DTOs).
     *
     * @param authorID The unique identifier (ID) of the author whose feedback entries are to
     *                 be retrieved.
     * @return A list of FeedbackDto objects representing feedback entries created by the
     * specified author.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getFeedbacksByAuthorID(int authorID) {

        List<Feedback> listOfFeedbacksByAuthorID = feedbackRepository.findFeedbackByAuthorID(authorID);

        return mapFeedbackListToFeedbackDto(listOfFeedbacksByAuthorID);
    }

    /**
     * Retrieves a list of feedback entries created before the specified creation date and maps
     * them to their corresponding Data Transfer Objects (DTOs).
     *
     * @param creationDate The creation date before which feedback entries are to be retrieved.
     * @return A list of FeedbackDto objects representing feedback entries created before the
     * specified date.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getFeedbackBeforeCreationDate(LocalDate creationDate) {

        List<Feedback> listOfFeedbacksBeforeDate =
                feedbackRepository.findFeedbackByFeedbackCreationDateBefore(creationDate.atStartOfDay());

        return mapFeedbackListToFeedbackDto(listOfFeedbacksBeforeDate);
    }

    /**
     * Retrieves a list of feedback entries created after the specified creation date and maps
     * them to their corresponding Data Transfer Objects (DTOs).
     *
     * @param creationDate The creation date after which feedback entries are to be retrieved.
     * @return A list of FeedbackDto objects representing feedback entries created after the
     * specified date.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getFeedbackAfterCreationDate(LocalDate creationDate) {

        List<Feedback> listOfFeedbacksAfterDate =
                feedbackRepository.findFeedbackByFeedbackCreationDateAfter(creationDate.atStartOfDay());

        return mapFeedbackListToFeedbackDto(listOfFeedbacksAfterDate);
    }

    /**
     * Retrieves a list of feedback entries created on the specified creation date and maps
     * them to their corresponding Data Transfer Objects (DTOs).
     *
     * @param creationDate The specific creation date for which feedback entries are to be retrieved.
     * @return A list of FeedbackDto objects representing feedback entries created on the
     * specified date.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getFeedbackOnCreationDate(LocalDate creationDate) {

        List<Feedback> listOfFeedbackOnDate =
                feedbackRepository.findFeedbackByFeedbackCreationDateAfter(creationDate.atStartOfDay());

        return mapFeedbackListToFeedbackDto(listOfFeedbackOnDate);
    }

    /**
     * Retrieves a list of feedback entries containing the specified word in their content
     * and maps them to their corresponding Data Transfer Objects (DTOs).
     *
     * @param word The word to be searched within the feedback content.
     * @return A list of FeedbackDto objects representing feedback entries containing the
     * specified word in their content.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackRepository
     * @see #mapFeedbackListToFeedbackDto(List)
     */
    @Override
    public List<FeedbackDto> getFeedbackByContainingWord(String word) {

        List<Feedback> listOfFeedbacksContainingWord = feedbackRepository.findFeedbackByFeedbackContentContaining(word);

        return mapFeedbackListToFeedbackDto(listOfFeedbacksContainingWord);
    }

    /**
     * Maps a list of Feedback entities to their corresponding Data Transfer Objects (DTOs).
     *
     * @param listOfFeedbacks The list of Feedback entities to be mapped to DTOs.
     * @return A list of FeedbackDto objects representing the mapped feedback entities.
     * @see Feedback
     * @see FeedbackDto
     * @see FeedbackMapper
     * @see UserService
     */
    private List<FeedbackDto> mapFeedbackListToFeedbackDto(List<Feedback> listOfFeedbacks) {

        return listOfFeedbacks.stream()
                .map(feedback -> {
                    UserDto feedbackAuthor = userService.getUserByID(feedback.getAuthorID());
                    return feedbackMapper.mapFeedbackToFeedbackDto(feedback, feedbackAuthor);
                })
                .collect(Collectors.toList());
    }
}
