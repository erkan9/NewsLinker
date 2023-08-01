package erkamber.services.implementations;

import erkamber.dtos.BookmarkDto;
import erkamber.entities.Bookmark;
import erkamber.exceptions.ForbiddenActionException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.BookmarkMapper;
import erkamber.repositories.BookmarkRepository;
import erkamber.services.interfaces.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, BookmarkMapper bookmarkMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public int addBookmark(BookmarkDto bookmarkDto) {
        Bookmark bookmark = this.bookmarkMapper.mapBookmarkDtoToBookmark(bookmarkDto);

        bookmarkRepository.save(bookmark);

        return bookmark.getBookmarkID();
    }

    @Override
    public List<BookmarkDto> getBookmarksByUserID(int userID) {
        List<Bookmark> bookmarksByUserID = this.bookmarkRepository.getBookmarksByUserID(userID);

        return this.bookmarkMapper.mapListOfBookmarksToBookmarksDtos(bookmarksByUserID);
    }

    @Override
    public List<BookmarkDto> getBookmarksByNewsID(int newsID) {
        List<Bookmark> bookmarksByNewsID = this.bookmarkRepository.getBookmarksByNewsID(newsID);

        return this.bookmarkMapper.mapListOfBookmarksToBookmarksDtos(bookmarksByNewsID);
    }

    @Override
    public BookmarkDto getBookmarkByUserIDAndNewsID(int userID, int newsID) {
        Bookmark bookmarkByUserIDAndNewsID = this.bookmarkRepository.getBookmarkByUserIDAndNewsID(userID, newsID);

        return this.bookmarkMapper.mapBookmarkToBookmarkDto(bookmarkByUserIDAndNewsID);
    }

    public BookmarkDto getBookmarkDtoByBookmarkID(int bookmarkID) {

        Bookmark bookmark = this.bookmarkRepository.findById(bookmarkID).orElseThrow(() -> {
            throw new ResourceNotFoundException("Bookmark not Found: " + bookmarkID, "Bookmark");
        });

        return this.bookmarkMapper.mapBookmarkToBookmarkDto(bookmark);
    }

    @Override
    public BookmarkDto deleteBookmarkByIDAndUserID(int userId, int bookmarkID) {
        BookmarkDto bookmarkByID = getBookmarkDtoByBookmarkID(bookmarkID);

        if (bookmarkByID.getUserID() != userId) {
            throw new ForbiddenActionException(
                    "User with ID: " + userId + " does not have permission to delete bookmark with ID: " +
                    bookmarkByID);
        }

        this.bookmarkRepository.deleteById(bookmarkID);
        return bookmarkByID;
    }

    @Override
    public BookmarkDto deleteBookmarkByID(int bookmarkID) {
        BookmarkDto bookmarkByID = getBookmarkDtoByBookmarkID(bookmarkID);

        this.bookmarkRepository.deleteById(bookmarkID);
        return bookmarkByID;
    }
}
