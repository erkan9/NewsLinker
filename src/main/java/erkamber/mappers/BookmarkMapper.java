package erkamber.mappers;

import erkamber.dtos.BookmarkDetailedDto;
import erkamber.dtos.BookmarkDto;
import erkamber.dtos.NewsDetailedDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    public BookmarkDto mapBookmarkToBookmarkDto(Bookmark bookmark) {

        return new BookmarkDto(bookmark.getBookmarkID(), bookmark.getUserID(), bookmark.getNewsID());
    }

    public Bookmark mapBookmarkDtoToBookmark(BookmarkDto bookmarkDto) {

        return new Bookmark(bookmarkDto.getBookmarkID(), bookmarkDto.getUserID(), bookmarkDto.getNewsID());
    }

    public BookmarkDetailedDto mapToBookmarkDetailedDto(int bookmarkID, UserDto userDto, NewsDetailedDto newsDetailedDto) {

        return new BookmarkDetailedDto(bookmarkID, userDto, newsDetailedDto);
    }
}
