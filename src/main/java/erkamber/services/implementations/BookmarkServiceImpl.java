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

    /**
     * Adds a new bookmark based on the information provided in the {@link BookmarkDto} object.
     * The bookmark is mapped from the provided DTO and then saved using the bookmarkRepository.
     *
     * @param bookmarkDto The {@link BookmarkDto} object containing the information for the new bookmark.
     * @return The ID of the newly added bookmark.
     */
    @Override
    public int addBookmark(BookmarkDto bookmarkDto) {
        Bookmark bookmark = this.bookmarkMapper.mapBookmarkDtoToBookmark(bookmarkDto);

        bookmarkRepository.save(bookmark);

        return bookmark.getBookmarkID();
    }

    /**
     * Retrieves a list of bookmarks associated with the specified user ID.
     *
     * @param userID The ID of the user whose bookmarks are to be retrieved.
     * @return A list of {@link BookmarkDto} objects representing the bookmarks associated with the user.
     */
    @Override
    public List<BookmarkDto> getBookmarksByUserID(int userID) {
        List<Bookmark> bookmarksByUserID = this.bookmarkRepository.getBookmarksByUserID(userID);

        return this.bookmarkMapper.mapListOfBookmarksToBookmarksDtos(bookmarksByUserID);
    }

    /**
     * Retrieves a list of bookmarks associated with the specified news article ID.
     *
     * @param newsID The ID of the news article for which bookmarks are to be retrieved.
     * @return A list of {@link BookmarkDto} objects representing the bookmarks associated with the news article.
     */
    @Override
    public List<BookmarkDto> getBookmarksByNewsID(int newsID) {
        List<Bookmark> bookmarksByNewsID = this.bookmarkRepository.getBookmarksByNewsID(newsID);

        return this.bookmarkMapper.mapListOfBookmarksToBookmarksDtos(bookmarksByNewsID);
    }

    /**
     * Retrieves a bookmark associated with the specified user ID and news article ID.
     *
     * @param userID The ID of the user who bookmarked the news article.
     * @param newsID The ID of the news article for which the bookmark is to be retrieved.
     * @return A {@link BookmarkDto} object representing the bookmark associated with the user and news article,
     * or {@code null} if no bookmark exists for the given user and news article.
     */
    @Override
    public BookmarkDto getBookmarkByUserIDAndNewsID(int userID, int newsID) {
        Bookmark bookmarkByUserIDAndNewsID = this.bookmarkRepository.getBookmarkByUserIDAndNewsID(userID, newsID);

        return this.bookmarkMapper.mapBookmarkToBookmarkDto(bookmarkByUserIDAndNewsID);
    }

    /**
     * Retrieves a bookmark DTO associated with the specified bookmark ID.
     *
     * @param bookmarkID The ID of the bookmark to be retrieved.
     * @return A {@link BookmarkDto} object representing the bookmark with the given ID.
     * @throws ResourceNotFoundException If no bookmark is found with the provided bookmark ID.
     */
    public BookmarkDto getBookmarkDtoByBookmarkID(int bookmarkID) {

        Bookmark bookmark = this.bookmarkRepository.findById(bookmarkID).orElseThrow(() -> {
            throw new ResourceNotFoundException("Bookmark not Found: " + bookmarkID, "Bookmark");
        });

        return this.bookmarkMapper.mapBookmarkToBookmarkDto(bookmark);
    }

    /**
     * Deletes a bookmark associated with the specified user ID and bookmark ID.
     *
     * @param userId     The ID of the user who owns the bookmark to be deleted.
     * @param bookmarkID The ID of the bookmark to be deleted.
     * @return The {@link BookmarkDto} object representing the deleted bookmark.
     * @throws ForbiddenActionException  If the user with the provided ID does not have permission to delete the bookmark.
     * @throws ResourceNotFoundException If no bookmark is found with the provided bookmark ID.
     */
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

    /**
     * Deletes a bookmark by its specified bookmark ID.
     *
     * @param bookmarkID The ID of the bookmark to be deleted.
     * @return The {@link BookmarkDto} object representing the deleted bookmark.
     * @throws ResourceNotFoundException If no bookmark is found with the provided bookmark ID.
     */
    @Override
    public BookmarkDto deleteBookmarkByID(int bookmarkID) {
        BookmarkDto bookmarkByID = getBookmarkDtoByBookmarkID(bookmarkID);

        this.bookmarkRepository.deleteById(bookmarkID);
        return bookmarkByID;
    }

    /**
     * Deletes all bookmarks associated with the specified user ID.
     *
     * @param userID The ID of the user whose bookmarks are to be deleted.
     */
    @Override
    public void deleteBookmarkByUserID(int userID) {

        List<Bookmark> bookmarksByUserID = bookmarkRepository.getBookmarksByUserID(userID);

        bookmarkRepository.deleteAll(bookmarksByUserID);
    }

    /**
     * Deletes all bookmarks associated with the specified news article ID.
     *
     * @param newsID The ID of the news article whose bookmarks are to be deleted.
     */
    @Override
    public void deleteBookmarkByNewsID(int newsID) {

        List<Bookmark> bookmarksByNewsID = bookmarkRepository.getBookmarksByNewsID(newsID);

        bookmarkRepository.deleteAll(bookmarksByNewsID);
    }
}
