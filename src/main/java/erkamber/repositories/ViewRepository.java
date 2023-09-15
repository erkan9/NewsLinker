package erkamber.repositories;

import erkamber.entities.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ViewRepository extends JpaRepository<View, Integer> {

    List<View> findViewByViewNewsID(int newsID);

    List<View> findViewByViewNewsIDAndViewCreationDate(int newsID, LocalDate creationDate);

    List<View> findViewByViewNewsIDAndViewCreationDateBefore(int newsID, LocalDate beforeCreationDate);

    List<View> findViewByViewNewsIDAndViewCreationDateAfter(int newsID, LocalDate afterCreationDate);

    List<View> findViewByViewCreationDateBetween(LocalDate startDate, LocalDate endDate);

    List<View> findViewByViewUserID(int userID);
}
