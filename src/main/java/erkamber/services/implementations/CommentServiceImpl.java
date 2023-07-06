package erkamber.services.implementations;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.mappers.CommentMapper;
import erkamber.repositories.CommentRepository;
import erkamber.services.interfaces.CommentService;
import erkamber.validations.InjectionValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final InjectionValidation injectionValidation;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper,
                              InjectionValidation injectionValidation) {

        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.injectionValidation = injectionValidation;
    }

    @Override
    public int addNewComment(CommentDto commentDto) {
        return 0;
    }

    @Override
    public int addCommentUpVote(int commentID, int userID) {

        //TODO need Upvote/downvote entity to keep track if user tries to vote something more than once
        return 0;
    }

    @Override
    public int addCommentDownVote(int commentID, int userID) {
        return 0;
    }

    @Override
    public void updateCommentContent(int commentID, int userID, String newContent) {

    }

    @Override
    public void deleteCommentByCommentID(int commentID) {

    }

    @Override
    public void deleteCommentByUserID(int userID) {

    }

    @Override
    public CommentDetailedDto getCommentByID(int commentID) {
        return null;
    }

    @Override
    public List<CommentDetailedDto> getCommentsByUserID(int userID) {
        return null;
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDate(LocalDate creationDate) {
        return null;
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDateBefore(LocalDate creationDate) {
        return null;
    }

    @Override
    public List<CommentDetailedDto> getCommentsByCreationDateAfter(LocalDate creationDate) {
        return null;
    }

    @Override
    public List<CommentDetailedDto> getAllComments() {
        return null;
    }
}
