package erkamber.services.implementations;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.entities.Comment;
import erkamber.entities.User;
import erkamber.exceptions.AuthorMismatchException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.exceptions.TextInjectionException;
import erkamber.mappers.CommentMapper;
import erkamber.repositories.CommentRepository;
import erkamber.repositories.UserRepository;
import erkamber.services.interfaces.CommentService;
import erkamber.validations.CommentValidation;
import erkamber.validations.InjectionValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final InjectionValidation injectionValidation;

    private final UserRepository userRepository;

    private final CommentValidation commentValidation;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper,
                              InjectionValidation injectionValidation, UserRepository userRepository,
                              CommentValidation commentValidation) {

        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.injectionValidation = injectionValidation;
        this.userRepository = userRepository;
        this.commentValidation = commentValidation;
    }

    @Override
    public int addNewComment(CommentDto commentDto) {

        isCommentContentValid(commentDto.getCommentContent());

        Comment newComment = commentMapper.mapCommentDtoToComment(commentDto);

        commentRepository.save(newComment);

        return newComment.getCommentID();
    }

    protected void addCommentUpVote(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        int numberOfUpVotes = searchedComment.getCommentUpVotes();

        searchedComment.setCommentUpVotes(numberOfUpVotes + 1);
    }

    protected void updateCommentVoteByRemovingVote(int commentID, boolean isUpvote) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        if (isUpvote) {

            int numberOfUpVotes = searchedComment.getCommentUpVotes();

            searchedComment.setCommentUpVotes(numberOfUpVotes - 1);

        } else {

            int numberOfDownVotes = searchedComment.getCommentDownVotes();

            searchedComment.setCommentDownVotes(numberOfDownVotes - 1);
        }

        commentRepository.save(searchedComment);
    }

    protected void updateCommentVoteBySwappingVotes(int commentID, boolean isUpVote) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        int numberOfUpVotes = searchedComment.getCommentUpVotes();

        int numberOfDownVotes = searchedComment.getCommentDownVotes();

        if (isUpVote) {

            searchedComment.setCommentUpVotes(numberOfUpVotes + 1);

            searchedComment.setCommentDownVotes(numberOfDownVotes - 1);

        } else {

            searchedComment.setCommentUpVotes(numberOfUpVotes - 1);

            searchedComment.setCommentDownVotes(numberOfDownVotes + 1);
        }

        commentRepository.save(searchedComment);
    }

    protected void addCommentDownVote(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        int numberOfDownVotes = searchedComment.getCommentDownVotes();

        searchedComment.setCommentDownVotes(numberOfDownVotes + 1);
    }

    @Override
    public void updateCommentContent(int commentID, int userID, String newContent) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        isUserIDMatchingCommentAuthorID(searchedComment.getCommentAuthorID(), userID);

        searchedComment.setCommentContent(newContent);

        commentRepository.save(searchedComment);
    }

    @Override
    public void deleteCommentByCommentID(int commentID) {

        commentRepository.deleteById(commentID);
    }

    @Override
    public int deleteAllCommentsOfUser(int userID) {

        int deletedCommentsCounter = 0;

        List<Comment> listOfCommentsByUser = commentRepository.findCommentsByCommentAuthorID(userID);

        for (Comment comment : listOfCommentsByUser) {

            commentRepository.deleteById(comment.getCommentID());

            deletedCommentsCounter++;
        }

        return deletedCommentsCounter;
    }

    @Override
    public CommentDetailedDto getCommentByID(int commentID) {

        Optional<Comment> optionalComment = commentRepository.findById(commentID);

        Comment searchedComment = optionalComment.orElseThrow(() ->
                new ResourceNotFoundException("Comment not Found:" + commentID, "Comment"));

        User author = convertUserIDToObject(searchedComment.getCommentAuthorID());

        return commentMapper.mapToCommentDetailedDto(searchedComment, author);
    }

    @Override
    public List<CommentDetailedDto> getCommentsByUserID(int userID) {

        List<Comment> listOfCommentsByuserID = commentRepository.findCommentsByCommentAuthorID(userID);

        return convertListToDetailedDto(listOfCommentsByuserID);
    }

    @Override
    public List<CommentDetailedDto> getCommentsByNewsID(int newsID) {

        List<Comment> listOfCommentsByNewsID = commentRepository.findCommentsByCommentNewsID(newsID);

        return convertListToDetailedDto(listOfCommentsByNewsID);
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDate(LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCreationDate(creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDateBefore(LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCreationDateBefore(creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDateAfter(LocalDate creationDate) {

        List<Comment> listOfCommentsByDate = commentRepository.findCommentsByCreationDateAfter(creationDate);

        return convertListToDetailedDto(listOfCommentsByDate);
    }

    @Override
    public List<CommentDetailedDto> getAllComments() {

        List<Comment> listOfAllComments = commentRepository.findAll();

        return convertListToDetailedDto(listOfAllComments);
    }

    private List<CommentDetailedDto> convertListToDetailedDto(List<Comment> listOfComments) {

        List<CommentDetailedDto> listOfDetailedCommentDto = new ArrayList<>();

        for (Comment comment : listOfComments) {

            User author = convertUserIDToObject(comment.getCommentAuthorID());

            listOfDetailedCommentDto.add(commentMapper.mapToCommentDetailedDto(comment, author));
        }

        return listOfDetailedCommentDto;
    }

    private User convertUserIDToObject(int userID) {

        Optional<User> user = userRepository.findById(userID);

        return user.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + user, "User"));
    }

    private void isCommentContentValid(String commentContent) {

        if (injectionValidation.isTextContainingInjection(commentContent)) {

            throw new TextInjectionException("Comment content might contain Injection");
        }
    }

    private void isUserIDMatchingCommentAuthorID(int authorID, int userID) {

        if (commentValidation.isUserIDMatchingCommentAuthorID(authorID, userID)) {

            throw new AuthorMismatchException("Author ID of the comment does not match the provided User ID");
        }
    }
}
