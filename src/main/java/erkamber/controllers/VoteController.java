package erkamber.controllers;

import erkamber.dtos.VoteDto;
import erkamber.services.interfaces.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://news-linker-fe.vercel.app/"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }


    @PostMapping("/votes")
    public ResponseEntity<Void> addNewVote(@Valid @RequestBody VoteDto newVoteDto) {

        int newVoteDtoId = voteService.addNewVote(newVoteDto);

        return ResponseEntity.created(URI.create("api/v1/votes/" + newVoteDtoId)).build();
    }

    @GetMapping("/votes")
    public ResponseEntity<List<VoteDto>> getAllVotes() {

        return ResponseEntity.ok(voteService.getAllVotes());
    }

    @GetMapping("/upVotes")
    public ResponseEntity<List<VoteDto>> getAllUpVotes() {

        return ResponseEntity.ok(voteService.getAllUpVotes());
    }

    @GetMapping("/downVotes")
    public ResponseEntity<List<VoteDto>> getAllDownVotes() {

        return ResponseEntity.ok(voteService.getAllDownVotes());
    }

    @GetMapping(value = "/upVotes", params = {"userId"})
    public ResponseEntity<List<VoteDto>> getAllUpVotesByUserID(@RequestParam("userId")
                                                               @Positive(message = "User ID must be a Positive number!")
                                                               int userId) {

        return ResponseEntity.ok(voteService.getAllUpVotesByUserID(userId));
    }

    @GetMapping(value = "/downVotes", params = {"userId"})
    public ResponseEntity<List<VoteDto>> getAllDownVotesByUserID(@RequestParam("userId")
                                                                 @Positive(message = "User ID must be a Positive number!")
                                                                 int userId) {

        return ResponseEntity.ok(voteService.getAllDownVotesByUserID(userId));
    }

    @GetMapping(value = "/upVotes", params = {"contentID", "votedContentType"})
    public ResponseEntity<List<VoteDto>> getAllUpVotesByUserIDAndVotedContentType(@RequestParam("contentID")
                                                                                  @Positive(message = "Content ID must be a Positive number!")
                                                                                  int contentID,
                                                                                  @RequestParam("votedContentType")
                                                                                  @NotBlank(message = "Content Type cannot be Blank!")
                                                                                  @NotEmpty(message = "Content Type cannot be Empty!")
                                                                                  @NotNull(message = "Content Type cannot be null!")
                                                                                  String votedContentType) {

        return ResponseEntity.ok(voteService.getAllUpVotesByContentIDAndType(contentID, votedContentType));
    }

    @GetMapping(value = "/downVotes", params = {"contentID", "votedContentType"})
    public ResponseEntity<List<VoteDto>> getAllDownVotesByUserIDAndVotedContentType(@RequestParam("contentID")
                                                                                    @Positive(message = "Content ID must be a Positive number!")
                                                                                    int contentID,
                                                                                    @RequestParam("votedContentType")
                                                                                    @NotBlank(message = "Content Type cannot be Blank!")
                                                                                    @NotEmpty(message = "Content Type cannot be Empty!")
                                                                                    @NotNull(message = "Content Type cannot be null!")
                                                                                    String votedContentType) {

        return ResponseEntity.ok(voteService.getAllDownVotesByContentIDAndType(contentID, votedContentType));
    }


    @DeleteMapping(value = "/votes", params = {"votedContentType", "contentId"})
    public void deleteVotesByContentTypeAndContentID(
            @RequestParam("votedContentType")
            @NotBlank(message = "Content Type cannot be Blank!")
            @NotEmpty(message = "Content Type cannot be Empty!")
            @NotNull(message = "Content Type cannot be null!")
            String votedContentType,
            @RequestParam("contentId")
            @Positive(message = "Content ID must be a Positive number!")
            int contentID) {

        voteService.deleteAllVotesByContentTypeAndVotedContentID(votedContentType, contentID);
    }

    @DeleteMapping(value = "/votes", params = {"votedContentType", "userId"})
    public void deleteVotesByContentTypeAndUserID(
            @RequestParam("votedContentType")
            @NotBlank(message = "Content Type cannot be Blank!")
            @NotEmpty(message = "Content Type cannot be Empty!")
            @NotNull(message = "Content Type cannot be null!")
            String votedContentType,
            @RequestParam("userId")
            @Positive(message = "User ID must be a Positive number!")
            int userID) {

        voteService.deleteAllVotesByContentTypeAndUserID(votedContentType, userID);
    }

    @DeleteMapping("/vote")
    public void deleteVotesByUserID(@RequestParam @Positive(message = "User ID must be a positive number!") int userID) {

        voteService.deleteVotesUserID(userID);
    }
}
