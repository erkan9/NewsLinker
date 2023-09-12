package erkamber.services.implementations;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Comment;
import erkamber.entities.News;
import erkamber.exceptions.AuthorMismatchException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.exceptions.TextInjectionException;
import erkamber.mappers.CommentMapper;
import erkamber.repositories.CommentRepository;
import erkamber.repositories.NewsRepository;
import erkamber.services.interfaces.CommentService;
import erkamber.services.interfaces.EmailService;
import erkamber.validations.CommentValidation;
import erkamber.validations.InjectionValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final CommentValidation commentValidation;

    private final UserServiceImpl userService;

    private final NewsRepository newsRepository;

    private final EmailService emailService;

    private final InjectionValidation injectionValidation;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper,
                              CommentValidation commentValidation, UserServiceImpl userService, NewsRepository newsRepository,
                              EmailService emailService, InjectionValidation injectionValidation) {

        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentValidation = commentValidation;
        this.userService = userService;
        this.newsRepository = newsRepository;
        this.emailService = emailService;
        this.injectionValidation = injectionValidation;
    }


    /**
     * Adds a new comment based on the information provided in the {@link CommentDto} object and sends an email notification
     * to the author of the news article.
     *
     * @param commentDto The {@link CommentDto} object containing the information for the new comment.
     * @return The ID of the newly added comment.
     * @throws MessagingException        If there was an issue sending the email notification.
     * @throws TextInjectionException    If the comment content is invalid.
     * @throws ResourceNotFoundException If the news associated with the comment is not found.
     */
    @Override
    public int addNewComment(CommentDto commentDto) throws MessagingException {

        // Set the creation date of the comment to the current date
        commentDto.setCreationDate(LocalDateTime.now());

        // Validate the comment content
        isCommentContentValid(commentDto.getCommentContent());

        // Map the CommentDto to a Comment entity
        Comment newComment = commentMapper.mapCommentDtoToComment(commentDto);

        // Save the new comment using the commentRepository
        commentRepository.save(newComment);

        // Retrieve the username of the comment author
        String commentAuthorUserName = userService.getUserNameOfCommentAuthor(commentDto.getCommentAuthorID());

        // Retrieve the news associated with the comment
        Optional<News> searchedNewsOptional = newsRepository.findById(commentDto.getCommentNewsID());

        // Retrieve the user information of the news article's author
        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + commentDto.getCommentNewsID(), "News"));

        // Retrieve the user information of the news article's author
        UserDto newsAuthor = userService.getUserByID(searchedNews.getUserID());

        // Send an email notification to the news author
       // emailService.sendEmail(newsAuthor.getUserFirstName(), commentAuthorUserName, commentDto.getCommentContent(),
       //         newsAuthor.getUserEmail(), searchedNews.getNewsTitle());

        // Return the ID of the newly added comment
        return newComment.getCommentID();
    }

    /**
     * Adds an upvote to the specified comment and updates the comment's upvote count.
     *
     * @param commentID The ID of the comment to which an upvote should be added.
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    protected void addCommentUpVote(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        int numberOfUpVotes = searchedComment.getCommentUpVotes();

        searchedComment.setCommentUpVotes(numberOfUpVotes + 1);

        commentRepository.save(searchedComment);
    }

    /**
     * Updates the vote count of a comment by removing a vote (either upvote or downvote).
     *
     * @param commentID The ID of the comment for which the vote should be removed.
     * @param isUpvote  A flag indicating whether the removed vote is an upvote (true) or a downvote (false).
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    @Transactional
    protected void updateCommentVoteByRemovingVote(int commentID, boolean isUpvote) {

        // Retrieve the comment by its ID
        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        // Update the appropriate vote count based on the isUpvote flag
        if (isUpvote) {

            int numberOfUpVotes = searchedComment.getCommentUpVotes();

            searchedComment.setCommentUpVotes(numberOfUpVotes - 1);

        } else {

            int numberOfDownVotes = searchedComment.getCommentDownVotes();

            searchedComment.setCommentDownVotes(numberOfDownVotes - 1);
        }

        // Save the updated comment in the repository
        commentRepository.save(searchedComment);
    }

    /**
     * Updates the vote count of a comment by swapping an upvote to a downvote or vice versa.
     *
     * @param commentID The ID of the comment for which the vote should be swapped.
     * @param isUpVote  A flag indicating whether the vote to be swapped is an upvote (true) or a downvote (false).
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    @Transactional
    protected void updateCommentVoteBySwappingVotes(int commentID, boolean isUpVote) {
        // Retrieve the comment by its ID
        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        // Get the current upvote and downvote counts
        int numberOfUpVotes = searchedComment.getCommentUpVotes();
        int numberOfDownVotes = searchedComment.getCommentDownVotes();

        // Swap the vote from upvote to downvote or vice versa
        if (isUpVote) {
            searchedComment.setCommentUpVotes(numberOfUpVotes + 1);
            searchedComment.setCommentDownVotes(numberOfDownVotes - 1);
        } else {
            searchedComment.setCommentUpVotes(numberOfUpVotes - 1);
            searchedComment.setCommentDownVotes(numberOfDownVotes + 1);
        }

        // Save the updated comment in the repository
        commentRepository.save(searchedComment);
    }

    /**
     * Adds a downvote to the specified comment and updates the comment's downvote count.
     *
     * @param commentID The ID of the comment to which a downvote should be added.
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    protected void addCommentDownVote(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        int numberOfDownVotes = searchedComment.getCommentDownVotes();

        searchedComment.setCommentDownVotes(numberOfDownVotes + 1);
    }

    /**
     * Updates the content of a comment with the specified new content, provided that the comment's author ID matches
     * the provided user ID.
     *
     * @param commentID  The ID of the comment to be updated.
     * @param userID     The ID of the user performing the update.
     * @param newContent The new content to replace the existing content of the comment.
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    @Override
    public void updateCommentContent(int commentID, int userID, String newContent) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        isUserIDMatchingCommentAuthorID(searchedComment.getCommentAuthorID(), userID);

        searchedComment.setCommentContent(newContent);

        commentRepository.save(searchedComment);
    }

    /**
     * Deletes a comment based on the specified comment ID.
     *
     * @param commentID The ID of the comment to be deleted.
     */
    @Override
    public void deleteCommentByCommentID(int commentID) {

        commentRepository.deleteById(commentID);
    }


    /**
     * Deletes all comments created by a specific user based on the provided user ID.
     *
     * @param userID The ID of the user whose comments are to be deleted.
     */
    @Override
    public void deleteAllCommentsOfUser(int userID) {

        List<Comment> listOfCommentsByUser = commentRepository.findCommentsByCommentAuthorIDOrderByCommentIDAsc(userID);

        for (Comment comment : listOfCommentsByUser) {

            commentRepository.deleteById(comment.getCommentID());
        }
    }

    /**
     * Deletes all comments associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose comments are to be deleted.
     */
    @Override
    public void deleteAllCommentsOfNewsID(int newsID) {

        List<Comment> listOfCommentsByNews = commentRepository.findCommentsByCommentNewsID(newsID);

        for (Comment comment : listOfCommentsByNews) {

            commentRepository.deleteById(comment.getCommentID());
        }
    }

    /**
     * Retrieves a detailed representation of a comment based on the specified comment ID.
     *
     * @param commentID The ID of the comment to be retrieved.
     * @return A {@link CommentDetailedDto} object representing the detailed information of the comment.
     * @throws ResourceNotFoundException If the comment with the provided ID is not found.
     */
    @Override
    public CommentDetailedDto getCommentByID(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        UserDto author = userService.getUserByID(searchedComment.getCommentAuthorID());

        return commentMapper.mapToCommentDetailedDto(searchedComment, author);
    }

    /**
     * Retrieves a list of detailed comment representations associated with the specified user ID.
     *
     * @param userID The ID of the user whose comments are to be retrieved.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments associated
     * with the user.
     */
    @Override
    public List<CommentDetailedDto> getCommentsByUserID(int userID) {

        List<Comment> listOfCommentsByuserID = commentRepository.findCommentsByCommentAuthorIDOrderByCommentIDAsc(userID);

        return convertListToDetailedDto(listOfCommentsByuserID);
    }

    /**
     * Retrieves a list of detailed comment representations associated with the specified news article ID.
     *
     * @param newsID The ID of the news article whose comments are to be retrieved.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments associated
     * with the news article.
     */
    @Override
    public List<CommentDetailedDto> getCommentsByNewsID(int newsID) {

        List<Comment> listOfCommentsByNewsID = commentRepository.findCommentsByCommentNewsIDOrderByCommentIDAsc(newsID);

        return convertListToDetailedDto(listOfCommentsByNewsID);
    }

    /**
     * Retrieves a list of detailed comment by newsID representations created on the specified creation date.
     *
     * @param newsID  newsID used to find its comments
     * @param creationDate The creation date for which comments are to be retrieved.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments created on
     * the specified date.
     */
    @Override
    public List<CommentDetailedDto> getCommentsByNewsIDAndIDCreationDate(int newsID, LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCommentNewsIDAndCreationDateOrderByCommentIDAsc(newsID, creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    /**
     * Retrieves a list of detailed comment by newsID representations created before the specified creation date.
     *
     * @param newsID  newsID used to find its comments
     * @param creationDate The reference creation date for which comments created before are to be retrieved.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments created
     * before the specified date.
     */
    @Override
    public List<CommentDetailedDto> getCommentsByNewsIDAndCreationDateBefore(int newsID, LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCommentNewsIDAndCreationDateBeforeOrderByCommentIDAsc(newsID, creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    /**
     * Retrieves a list of detailed comment by newsID representations created after the specified creation date.
     *
     * @param newsID  newsID used to find its comments
     * @param creationDate The reference creation date for which comments created after are to be retrieved.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments created
     * after the specified date.
     */
    @Override
    public List<CommentDetailedDto> getCommentsByNewsIDAndCreationDateAfter(int newsID, LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCommentNewsIDAndCreationDateAfterOrderByCommentIDAsc(newsID, creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    /**
     * Retrieves a list of detailed comment representations for all comments.
     *
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of all comments.
     */
    @Override
    public List<CommentDetailedDto> getAllComments() {

        List<Comment> listOfAllComments = commentRepository.findAll((Sort.by(Sort.Direction.ASC, "commentID")));

        return convertListToDetailedDto(listOfAllComments);
    }

    /**
     * Converts a list of Comment entities to a list of detailed comment representations with author information.
     *
     * @param listOfComments The list of Comment entities to be converted.
     * @return A list of {@link CommentDetailedDto} objects representing the detailed information of comments with author
     * information.
     */
    private List<CommentDetailedDto> convertListToDetailedDto(List<Comment> listOfComments) {

        List<CommentDetailedDto> listOfDetailedCommentDto = new ArrayList<>();

        // Convert each comment entity to a detailed DTO with author information
        for (Comment comment : listOfComments) {

            UserDto author = userService.getUserByID(comment.getCommentAuthorID());

            listOfDetailedCommentDto.add(commentMapper.mapToCommentDetailedDto(comment, author));
        }

        return listOfDetailedCommentDto;
    }

    /**
     * Validates the content of a comment to ensure it does not contain any potential injection vulnerabilities.
     *
     * @param commentContent The content of the comment to be validated.
     * @throws TextInjectionException If the comment content is found to potentially contain injection vulnerabilities.
     */
    private void isCommentContentValid(String commentContent) {

        if (injectionValidation.isTextContainingInjection(commentContent)) {

            throw new TextInjectionException("Comment content might contain Injection");
        }
        if (injectionValidation.isTextContainingSqlInjection(commentContent)) {

            throw new TextInjectionException("Comment content might contain SQL Injection");
        }
    }

    /**
     * Validates whether the provided user ID matches the author ID of a comment.
     *
     * @param authorID The author ID of the comment.
     * @param userID   The user ID to be compared with the comment's author ID.
     * @throws AuthorMismatchException If the provided user ID does not match the author ID of the comment.
     */
    private void isUserIDMatchingCommentAuthorID(int authorID, int userID) {

        if (commentValidation.isUserIDMatchingCommentAuthorID(authorID, userID)) {

            throw new AuthorMismatchException("Author ID of the comment does not match the provided User ID");
        }
    }
}
