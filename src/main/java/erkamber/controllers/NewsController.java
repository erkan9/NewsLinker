package erkamber.controllers;

import erkamber.dtos.NewsDetailedDto;
import erkamber.dtos.NewsDto;
import erkamber.services.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/news/{newsId}")
    public ResponseEntity<NewsDetailedDto> getNewsByNewsID(@PathVariable int newsId) {
        NewsDetailedDto newsDetailedByNewsID = this.newsService.getNewsDetailedByNewsID(newsId);

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


}
