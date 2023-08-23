package erkamber.controllers;

import erkamber.dtos.BookmarkDto;
import erkamber.services.interfaces.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://news-linker-fe.vercel.app/"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<Void> addBookmark(@RequestBody @Valid BookmarkDto bookmarkDto) {
        int bookmarkID = this.bookmarkService.addBookmark(bookmarkDto);

        URI location = UriComponentsBuilder
                .fromUriString("/bookmarks/{id}")
                .buildAndExpand(bookmarkID)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{userId}/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getBookmarksByUserID(@PathVariable int userId) {
        List<BookmarkDto> bookmarksByUserID = this.bookmarkService.getBookmarksByUserID(userId);

        return ResponseEntity.ok(bookmarksByUserID);
    }

    @GetMapping("/news/{newsId}/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getBookmarksByNewsID(@PathVariable int newsId) {
        List<BookmarkDto> bookmarksByNewsID = this.bookmarkService.getBookmarksByNewsID(newsId);

        return ResponseEntity.ok(bookmarksByNewsID);
    }

    @GetMapping("/users/{userId}/news/{newsId}/bookmarks")
    public ResponseEntity<BookmarkDto> getBookmarkByUserIDAndNewsID(@PathVariable int userId, @PathVariable int newsId) {
        BookmarkDto bookmarkByUserIDAndNewsID = this.bookmarkService.getBookmarkByUserIDAndNewsID(userId, newsId);

        return ResponseEntity.ok(bookmarkByUserIDAndNewsID);
    }

    @DeleteMapping("/users/{userId}/bookmarks/{bookmarkId}")
    public ResponseEntity<BookmarkDto> deleteBookmarksByIDAndUserID(@PathVariable int userId, @PathVariable int bookmarkId) {
        BookmarkDto bookmarkDto = this.bookmarkService.deleteBookmarkByIDAndUserID(userId, bookmarkId);

        return ResponseEntity.ok(bookmarkDto);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public ResponseEntity<BookmarkDto> deleteBookmarksByID(@PathVariable int bookmarkId) {
        BookmarkDto bookmarkDto = this.bookmarkService.deleteBookmarkByID(bookmarkId);

        return ResponseEntity.ok(bookmarkDto);
    }

    @DeleteMapping(value = "/bookmarks", params = {"newsId"})
    public void deleteBookmarkByNewsID(@RequestParam("newsId")
                                      @Positive(message = "News ID must be a Positive number!")
                                      int newsID) {

        this.bookmarkService.deleteBookmarkByNewsID(newsID);
    }

    @DeleteMapping(value = "/bookmarks", params = {"userId"})
    public void deleteBookmarkByUserID(@RequestParam("userId")
                                      @Positive(message = "User ID must be a Positive number!")
                                      int userID) {

        this.bookmarkService.deleteBookmarkByUserID(userID);
    }
}
