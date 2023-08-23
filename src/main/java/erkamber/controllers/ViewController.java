package erkamber.controllers;


import erkamber.dtos.ViewDetailedDto;
import erkamber.dtos.ViewDto;
import erkamber.services.interfaces.ViewService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://newslinker-backend.onrender.com"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class ViewController {

    private final ViewService viewService;

    public ViewController(ViewService viewService) {
        this.viewService = viewService;
    }

    @PostMapping("/views")
    public ResponseEntity<Void> addNewView(@Valid @RequestBody ViewDto viewDto) {

        int newViewID = viewService.addNewView(viewDto);


        return ResponseEntity.created(URI.create("api/v1/views/" + newViewID)).build();
    }

    @GetMapping("/views")
    public ResponseEntity<List<ViewDetailedDto>> getAllViews() {

        return ResponseEntity.ok(viewService.getAllViews());
    }

    @GetMapping(value = "/views/count", params = {"newsId"})
    public ResponseEntity<Integer> getNumberOfViewsOfNews(@RequestParam("newsId")
                                                          @Positive(message = "News ID must be a Positive number!")
                                                          int newsID) {

        return ResponseEntity.ok(viewService.getNumberOfViewsOfNews(newsID));
    }

    @GetMapping(value = "/views", params = {"userId"})
    public ResponseEntity<List<ViewDetailedDto>> findViewByViewUserID(@RequestParam("userId")
                                                                      @Positive(message = "User ID must be a Positive number!")
                                                                      int userID) {

        return ResponseEntity.ok(viewService.findViewByViewUserID(userID));
    }

    @GetMapping(value = "/views", params = {"newsId"})
    public ResponseEntity<List<ViewDetailedDto>> findViewByViewNewsID(@RequestParam("newsId")
                                                                      @Positive(message = "News ID must be a Positive number!")
                                                                      int newsID) {

        return ResponseEntity.ok(viewService.findViewByViewNewsID(newsID));
    }

    @GetMapping("/views/{viewId}")
    public ResponseEntity<ViewDetailedDto> getViewByID(@PathVariable int viewId) {

        return ResponseEntity.ok(viewService.getViewByID(viewId));
    }

    @DeleteMapping(value = "/views", params = {"newsId"})
    public void deleteViewByNewsID(@RequestParam("newsId")
                                   @Positive(message = "News ID must be a Positive number!")
                                   int newsID) {

        viewService.deleteViewsByNewsID(newsID);
    }

    @DeleteMapping(value = "/views", params = {"viewId"})
    public void deleteViewByViewID(@RequestParam("viewId")
                                   @Positive(message = "View ID must be a Positive number!")
                                   int viewID) {

        viewService.deleteViewsByViewID(viewID);
    }

    @DeleteMapping(value = "/views", params = {"userId"})
    public void deleteViewByUserID(@RequestParam("userId")
                                   @Positive(message = "User ID must be a Positive number!")
                                   int userID) {

        viewService.deleteViewsByUserID(userID);
    }

    @GetMapping(value = "/views", params = {"newsId", "creationDate"})
    public ResponseEntity<List<ViewDetailedDto>> findViewByNewsIDAndViewCreationDate(
            @RequestParam("newsId")
            @Positive(message = "News ID must be a Positive number!")
            int newsID,
            @RequestParam("creationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate creationDate) {


        return ResponseEntity.ok(viewService.findViewByViewNewsIDAndViewCreationDate(newsID, creationDate));
    }

    @GetMapping(value = "/views", params = {"newsId", "beforeCreationDate"})
    public ResponseEntity<List<ViewDetailedDto>> findViewByNewsIDAndViewCreationDateBefore(
            @RequestParam("newsId")
            @Positive(message = "News ID must be a Positive number!")
            int newsID,
            @RequestParam("beforeCreationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beforeCreationDate) {

        return ResponseEntity.ok(viewService.findViewByViewNewsIDAndViewCreationDateBefore(newsID, beforeCreationDate));
    }

    @GetMapping(value = "/views", params = {"newsId", "afterCreationDate"})
    public ResponseEntity<List<ViewDetailedDto>> findViewByNewsIDAndViewCreationDateAfter(
            @RequestParam("newsId")
            @Positive(message = "News ID must be a Positive number!")
            int newsID,
            @RequestParam("afterCreationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate afterCreationDate) {

        return ResponseEntity.ok(viewService.findViewByViewNewsIDAndViewCreationDateAfter(newsID, afterCreationDate));
    }
}
