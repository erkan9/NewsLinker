package erkamber.controllers;


import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.services.interfaces.CommentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://newslinker-backend.onrender.com"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> addNewComment(@Valid @RequestBody CommentDto commentDto) throws MessagingException {

        int newCommentDto = commentService.addNewComment(commentDto);

        return ResponseEntity.created(URI.create("api/v1/comments/" + newCommentDto)).build();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDetailedDto>> getAllComments() {

        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping(value = "/comments", params = {"beforeCreationDate"})
    public ResponseEntity<List<CommentDetailedDto>> getCommentsByCreationDateBefore(@RequestParam("beforeCreationDate")
                                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                    LocalDate beforeCreationDate) {

        return ResponseEntity.ok(commentService.getCommentsByCreationDateBefore(beforeCreationDate));
    }

    @GetMapping(value = "/comments", params = {"afterCreationDate"})
    public ResponseEntity<List<CommentDetailedDto>> getCommentsByCreationDateAfter(@RequestParam("afterCreationDate")
                                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                   LocalDate afterCreationDate) {

        return ResponseEntity.ok(commentService.getCommentsByCreationDateAfter(afterCreationDate));
    }

    @GetMapping(value = "/comments", params = {"creationDate"})
    public ResponseEntity<List<CommentDetailedDto>> getCommentsByCreationDate(@RequestParam("creationDate")
                                                                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                              LocalDate creationDate) {

        return ResponseEntity.ok(commentService.getCommentsByCreationDate(creationDate));
    }

    @GetMapping(value = "/comments", params = {"newsId"})
    public ResponseEntity<List<CommentDetailedDto>> getCommentsByNewsID(@RequestParam("newsId")
                                                                        @Positive(message = "News ID must be a Positive number!")
                                                                        int newsID) {

        return ResponseEntity.ok(commentService.getCommentsByNewsID(newsID));
    }

    @GetMapping(value = "/comments", params = {"userId"})
    public ResponseEntity<List<CommentDetailedDto>> getCommentsByUserID(@RequestParam("userId")
                                                                        @Positive(message = "User ID must be a Positive number!")
                                                                        int userID) {

        return ResponseEntity.ok(commentService.getCommentsByUserID(userID));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDetailedDto> getViewByID(@PathVariable int commentId) {

        return ResponseEntity.ok(commentService.getCommentByID(commentId));
    }

    @PatchMapping(value = "/comments", params = {"commentId", "userId"})
    public void updateCommentContent(
            @RequestParam("commentId")
            @Positive(message = "Comment ID must be a Positive number!")
            int commentID,
            @RequestParam("userId")
            @Positive(message = "User ID must be a Positive number!")
            int userID,
            @RequestBody String newContent) {

        commentService.updateCommentContent(commentID, userID, newContent);
    }

    @DeleteMapping(value = "/comments", params = {"commentId"})
    public void deleteCommentByID(@RequestParam("commentId")
                                  @Positive(message = "Comment ID must be a Positive number!")
                                  int commentID) {

        commentService.deleteCommentByCommentID(commentID);
    }

    @DeleteMapping(value = "/comments", params = {"userId"})
    public void deleteCommentByUserID(@RequestParam("userId")
                                      @Positive(message = "User ID must be a Positive number!")
                                      int UserID) {

        commentService.deleteAllCommentsOfUser(UserID);
    }

    @DeleteMapping(value = "/comments", params = {"newsId"})
    public void deleteCommentByNewsID(@RequestParam("newsId")
                                      @Positive(message = "News ID must be a Positive number!")
                                      int newsID) {

        commentService.deleteAllCommentsOfNewsID(newsID);
    }
}
