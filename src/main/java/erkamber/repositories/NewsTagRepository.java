package erkamber.repositories;

import erkamber.entities.NewsTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsTagRepository extends JpaRepository<NewsTag, Integer> {

    List<NewsTag> findNewsTagsByNewsIDAndTagID(int newsID, int tagID);

    List<NewsTag> findNewsTagsByNewsID(int newsID);

    List<NewsTag> findNewsTagsByTagID(int tagID);
}
