package erkamber.repositories;

import erkamber.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    List<News> findNewsByUserIDOrderByNewsIDAsc(int userID);

    List<News> findNewsByNewsTitleOrderByNewsIDAsc(String newsTitle);

    List<News> findNewsByNewsUpVotes(int upVotes);

    List<News> findNewsByNewsDownVotes(int downVotes);

    List<News> findNewsByUserIDAndNewsCreationDate(int userID, LocalDate creationDate);

    List<News> findNewsByNewsCreationDateBeforeOrderByNewsIDAsc(LocalDate beforeDate);

    List<News> findNewsByNewsCreationDateAfterOrderByNewsIDAsc(LocalDate afterDate);

    List<News> findNewsByNewsCreationDate(LocalDate creationDate);

    List<News> findNewsByNewsCreationDateBetween(LocalDate startDate, LocalDate endDate);

    List<News> findNewsByNewsContentContaining(String newsContent);
}