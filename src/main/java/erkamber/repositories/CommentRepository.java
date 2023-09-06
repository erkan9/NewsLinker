package erkamber.repositories;

import erkamber.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentsByCommentAuthorID(int authorID);

    List<Comment> findCommentsByCommentNewsIDOrderByCommentIDAsc(int newsID);

    List<Comment> findCommentsByCommentNewsID(int newsID);

    List<Comment> findCommentsByCreationDate(LocalDate creationDate);

    List<Comment> findCommentsByCreationDateBefore(LocalDate creationDate);

    List<Comment> findCommentsByCreationDateAfter(LocalDate creationDate);

    List<Comment> findCommentsByCommentContentContaining(String content);
}
