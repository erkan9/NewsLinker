package erkamber.services.interfaces;

import erkamber.dtos.BookmarkDto;

import java.util.List;

public interface BookmarkService {

    int addBookmark(BookmarkDto bookmarkDto);

    List<BookmarkDto> getBookmarksByUserID(int userID);

    List<BookmarkDto> getBookmarksByNewsID(int newsID);

    BookmarkDto getBookmarkByUserIDAndNewsID(int userID, int newsID);

    BookmarkDto deleteBookmarkByIDAndUserID(int userId, int bookmarkID);

    BookmarkDto deleteBookmarkByID(int bookmarkID);
}
