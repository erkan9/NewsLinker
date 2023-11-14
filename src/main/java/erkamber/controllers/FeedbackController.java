package erkamber.controllers;


import erkamber.dtos.FeedbackDto;
import erkamber.services.implementations.FeedbackServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://news-linker-fe.vercel.app"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

    public FeedbackController(FeedbackServiceImpl feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedbacks")
    public ResponseEntity<Void> addNewFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {

        int newFeedbackId = feedbackService.createFeedback(feedbackDto);

        return ResponseEntity.created(URI.create("api/v1/feedback/" + newFeedbackId)).build();
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {

        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping(value = "/feedbacks", params = {"authorId"})
    public ResponseEntity<List<FeedbackDto>> getFeedbackByAuthorId(@RequestParam("authorId")
                                                                   @Positive(message = "Author ID must be a Positive number!")
                                                                   int authorID) {

        return ResponseEntity.ok(feedbackService.getFeedbacksByAuthorID(authorID));
    }

    @DeleteMapping("/feedbacks/author/{authorId}")
    public ResponseEntity<Integer> deleteFeedbackByAuthorsID(@PathVariable int authorId) {

        return ResponseEntity.ok(feedbackService.deleteFeedbackByAuthorID(authorId));
    }

    @DeleteMapping(value = "/feedbacks", params = {"authorId"})
    public ResponseEntity<Integer> deleteFeedbackByID(@RequestParam("authorId")
                                                              @Positive(message = "Author ID must be a Positive number!")
                                                              int authorId) {

        return ResponseEntity.ok(feedbackService.deleteFeedbackByAuthorID(authorId));
    }

    @GetMapping(value = "/feedbacks", params = {"creationDate"})
    public ResponseEntity<List<FeedbackDto>> getFeedbacksOnCreationDate(@RequestParam("creationDate")
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                        LocalDate creationDate) {

        return ResponseEntity.ok(feedbackService.getFeedbackOnCreationDate(creationDate));
    }

    @GetMapping(value = "/feedbacks/after", params = {"creationDate"})
    public ResponseEntity<List<FeedbackDto>> getFeedbacksOnCreationDateAfter(@RequestParam("creationDate")
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                             LocalDate creationDate) {

        return ResponseEntity.ok(feedbackService.getFeedbackAfterCreationDate(creationDate));
    }

    @GetMapping(value = "/feedbacks/before", params = {"creationDate"})
    public ResponseEntity<List<FeedbackDto>> getFeedbacksOnCreationDateBefore(@RequestParam("creationDate")
                                                                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                              LocalDate creationDate) {

        return ResponseEntity.ok(feedbackService.getFeedbackBeforeCreationDate(creationDate));
    }

    @GetMapping("/comments/{feedbackId}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable int feedbackId) {

        return ResponseEntity.ok(feedbackService.getFeedbackById(feedbackId));
    }

    @DeleteMapping("/comments/{feedbackId}")
    public ResponseEntity<Integer> deleteByFeedbackId(@PathVariable int feedbackId) {

        return ResponseEntity.ok(feedbackService.deleteFeedbackByFeedbackID(feedbackId));
    }


    @GetMapping(value = "/feedbacks", params = {"contains"})
    public ResponseEntity<List<FeedbackDto>> getFeedbackByContainingWord(@RequestParam("contains")
                                                                         @NotNull(message = "Searched word cannot be Null!!")
                                                                         @NotEmpty(message = "Searched word cannot be Empty!!")
                                                                         @NotBlank(message = "Searched word cannot be Blank!!")
                                                                         String contains) {

        return ResponseEntity.ok(feedbackService.getFeedbackByContainingWord(contains));
    }
}
