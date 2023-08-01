package erkamber.repositories;

import erkamber.entities.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    List<Bookmark> getBookmarksByUserID(int userID);

    List<Bookmark> getBookmarksByNewsID(int newsID);

    Bookmark getBookmarkByUserIDAndNewsID(int userID, int newsID);
}
