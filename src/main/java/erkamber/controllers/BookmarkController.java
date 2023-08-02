package erkamber.controllers;

import erkamber.dtos.BookmarkDto;
import erkamber.services.interfaces.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
}
