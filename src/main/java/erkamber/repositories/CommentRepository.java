package erkamber.repositories;

import erkamber.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentsByCommentAuthorIDOrderByCommentIDAsc(int authorID);

    List<Comment> findCommentsByCommentNewsIDOrderByCommentIDAsc(int newsID);

    List<Comment> findCommentsByCommentNewsID(int newsID);

    List<Comment> findCommentsByCommentNewsIDAndCreationDateOrderByCommentIDAsc(int commentNewsID, LocalDateTime creationDate);

    List<Comment> findCommentsByCommentNewsIDAndCreationDateBeforeOrderByCommentIDAsc(int commentNewsID, LocalDateTime creationDate);

    List<Comment> findCommentsByCommentNewsIDAndCreationDateAfterOrderByCommentIDAsc(int commentNewsID, LocalDateTime creationDate);

    List<Comment> findCommentsByCommentContentContaining(String content);
}
