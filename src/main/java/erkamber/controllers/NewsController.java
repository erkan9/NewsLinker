package erkamber.controllers;

import erkamber.dtos.NewsDetailedDto;
import erkamber.dtos.NewsDto;
import erkamber.services.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://newslinker-backend.onrender.com"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/news")
    public ResponseEntity<Void> addNews(@RequestBody @Valid NewsDto newsDto) {
        int newsID = this.newsService.addNews(newsDto);

        URI location = UriComponentsBuilder
                .fromUriString("/news/{id}")
                .buildAndExpand(newsID)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/news")
    public ResponseEntity<List<NewsDetailedDto>> getAllNews() {
        List<NewsDetailedDto> allNews = this.newsService.getAllNews();

        return ResponseEntity.ok(allNews);
    }

    @GetMapping("/news/detailed/{newsId}")
    public ResponseEntity<NewsDetailedDto> getNewsDetailedByNewsID(@PathVariable int newsId) {
        NewsDetailedDto newsDetailedByNewsID = this.newsService.getNewsDetailedByNewsID(newsId);

        return ResponseEntity.ok(newsDetailedByNewsID);
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<NewsDto> getNewsByNewsID(@PathVariable int newsId) {
        NewsDto newsDetailedByNewsID = this.newsService.getNewsByNewsID(newsId);

        return ResponseEntity.ok(newsDetailedByNewsID);
    }

    @GetMapping("/users/{userId}/news/{newsId}")
    public ResponseEntity<NewsDetailedDto> getNewsAsLoggedUser(@PathVariable int userId, @PathVariable int newsId) {
        NewsDetailedDto newsAsLoggedUser = this.newsService.getNewsAsLoggedUser(userId, newsId);

        return ResponseEntity.ok(newsAsLoggedUser);
    }

    @GetMapping("/users/{userId}/news")
    public ResponseEntity<List<NewsDetailedDto>> getNewsByUserId(@PathVariable int userId) {
        List<NewsDetailedDto> newsByUserID = this.newsService.findNewsByUserID(userId);

        return ResponseEntity.ok(newsByUserID);
    }

    @GetMapping(value = "/news", params = {"title"})
    public ResponseEntity<List<NewsDetailedDto>> getNewsByTitle(@RequestParam String title) {
        List<NewsDetailedDto> newsByNewsTitle = this.newsService.findNewsByNewsTitle(title);

        return ResponseEntity.ok(newsByNewsTitle);
    }

    @GetMapping(value = "/news", params = {"creationDateBefore"})
    public ResponseEntity<List<NewsDetailedDto>> getNewsByCreationDateBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate creationDateBefore) {

        List<NewsDetailedDto> newsByCreationDateBefore = this.newsService.findNewsByCreationDateBefore(creationDateBefore);

        return ResponseEntity.ok(newsByCreationDateBefore);
    }

    @GetMapping(value = "/news", params = {"creationDateAfter"})
    public ResponseEntity<List<NewsDetailedDto>> getNewsByCreationDateAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate creationDateAfter) {

        List<NewsDetailedDto> newsByCreationDateBefore = this.newsService.findNewsByCreationDateAfter(creationDateAfter);

        return ResponseEntity.ok(newsByCreationDateBefore);
    }

    @PatchMapping(value = "/news/update/content", params = {"newsId", "userId"})
    public void updateNewsContent(
            @RequestParam("newsId")
            @Positive(message = "Comment ID must be a Positive number!")
            int newsID,
            @RequestParam("userId")
            @Positive(message = "User ID must be a Positive number!")
            int userID,
            @RequestBody String content) {

        newsService.updateNewsContent(content, newsID, userID);
    }


    @PatchMapping(value = "/news/update/title", params = {"newsId", "userId"})
    public void updateNewsTitle(
            @RequestParam("newsId")
            @Positive(message = "Comment ID must be a Positive number!")
            int newsID,
            @RequestParam("userId")
            @Positive(message = "User ID must be a Positive number!")
            int userID,
            @RequestBody String title) {

        newsService.updateNewsTitle(title, newsID, userID);
    }

    @DeleteMapping(value = "/news", params = {"userId"})
    public void deleteNewsByAuthorID(@RequestParam("userId")
                                     @Positive(message = "Author ID must be a Positive number!")
                                     int authorID) {

        newsService.deleteNewsByAuthorID(authorID);
    }

    @DeleteMapping(value = "/news", params = {"newsId"})
    public void deleteNewsByNewsID(@RequestParam("newsId")
                                   @Positive(message = "News ID must be a Positive number!")
                                   int newsID) {

        newsService.deleteNewsByID(newsID);
    }
}
