package erkamber.controllers;

import erkamber.dtos.NewsTagDetailedDto;
import erkamber.dtos.NewsTagDto;
import erkamber.dtos.NewsTagDtoResponse;
import erkamber.services.interfaces.NewsTagService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://news-linker-fe.vercel.app"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class NewsTagController {

    private final NewsTagService newsTagService;


    public NewsTagController(NewsTagService newsTagService) {
        this.newsTagService = newsTagService;
    }


    @PostMapping("/newsTags")
    public ResponseEntity<Void> addNewsTag(@Valid @RequestBody NewsTagDto newsTagDto) {

        int newsTagID = newsTagService.addNewsTag(newsTagDto);

        return ResponseEntity.created(URI.create("api/v1/newsTags/" + newsTagID)).build();
    }

    @GetMapping("/newsTags")
    public ResponseEntity<List<NewsTagDetailedDto>> getAllNewsTags() {

        return ResponseEntity.ok(newsTagService.getAllNewsTags());
    }

    @GetMapping(value = "/newsTags", params = {"newsId"})
    public ResponseEntity<List<NewsTagDtoResponse>> getTagsOfNews(@RequestParam("newsId")
                                                          @Positive(message = "News ID must be a Positive number!")
                                                          int newsID) {

        return ResponseEntity.ok(newsTagService.getTagsOfNews(newsID));
    }

    @GetMapping(value = "/newsTags/detailed", params = {"newsId"})
    public ResponseEntity<List<NewsTagDetailedDto>> getTagsOfNewsDetailed(@RequestParam("newsId")
                                                                          @Positive(message = "News ID must be a Positive number!")
                                                                          int newsID) {

        return ResponseEntity.ok(newsTagService.getTagsOfNewsDetailed(newsID));
    }

    @GetMapping(value = "/newsTags", params = {"tagId"})
    public ResponseEntity<List<NewsTagDetailedDto>> getNewsOfTag(@RequestParam("tagId")
                                                                 @Positive(message = "Tag ID must be a Positive number!")
                                                                 int tagID) {

        return ResponseEntity.ok(newsTagService.getNewsOfTag(tagID));
    }

    @DeleteMapping(value = "/newsTags", params = {"tagId"})
    public ResponseEntity<Integer> deleteNewsTagByTagID(@RequestParam("tagId")
                                                        @Positive(message = "Tag ID must be a Positive number!")
                                                        int tagId) {

        return ResponseEntity.ok(newsTagService.deleteNewsTagByTagID(tagId));
    }

    @DeleteMapping(value = "/newsTags", params = {"newsId"})
    public ResponseEntity<Integer> deleteNewsTagByNewsID(@RequestParam("newsId")
                                                         @Positive(message = "News ID must be a Positive number!")
                                                         int newsID) {

        return ResponseEntity.ok(newsTagService.deleteAllTagsByNewsID(newsID));
    }

    @DeleteMapping(value = "/newsTags", params = {"tagId", "newsId"})
    public void deleteNewsTagByTagIDAndNewsID(@RequestParam("tagId")
                                              @Positive(message = "Tag ID must be a Positive number!")
                                              int tagID,
                                              @RequestParam("newsId")
                                              @Positive(message = "News ID must be a Positive number!")
                                              int newsID) {

        newsTagService.deleteNewsTag(tagID, newsID);
    }

    @GetMapping("/newsTags/{newsTagID}")
    public ResponseEntity<NewsTagDto> getNewsTagByTagID(@PathVariable int newsTagID) {

        return ResponseEntity.ok(newsTagService.getNewsTagByID(newsTagID));
    }
}
