package erkamber.repositories;

import erkamber.entities.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ViewRepository extends JpaRepository<View, Integer> {

    List<View> findViewByViewNewsID(int postID);

    List<View> findViewByViewNewsIDAndViewCreationDate(int postID, LocalDate creationDate);

    List<View> findViewByViewNewsIDAndViewCreationDateBefore(int postID, LocalDate beforeCreationDate);

    List<View> findViewByViewNewsIDAndViewCreationDateAfter(int postID, LocalDate afterCreationDate);

    List<View> findViewByViewUserID(int userID);
}
