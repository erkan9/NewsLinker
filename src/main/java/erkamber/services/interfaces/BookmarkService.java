package erkamber.services.interfaces;

import erkamber.dtos.BookmarkDetailedDto;
import erkamber.dtos.BookmarkDto;

import java.util.List;

public interface BookmarkService {

    BookmarkDto addBookmark(BookmarkDto bookmarkDto);

    List<BookmarkDetailedDto> getBookmarksByUserID(int userID);

    List<BookmarkDetailedDto> getBookmarksByNewsID(int newsID);

    BookmarkDto deleteBookmarkByIDAndUserID(int userId, int bookmarkID);

    BookmarkDto deleteBookmarkByID(int bookmarkID);
}
