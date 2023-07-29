package erkamber.controllers;

import erkamber.configurations.HttpHeadersConfiguration;
import erkamber.dtos.VoteDto;
import erkamber.services.interfaces.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class VoteController {

    private final VoteService voteService;

    private final HttpHeadersConfiguration httpHeadersConfiguration;

    public VoteController(VoteService voteService, HttpHeadersConfiguration httpHeadersConfiguration) {
        this.voteService = voteService;
        this.httpHeadersConfiguration = httpHeadersConfiguration;
    }

    @PostMapping("/votes")
    public ResponseEntity<Void> addNewVote(@Valid @RequestBody VoteDto newVoteDto) {

        int newVoteDtoId = voteService.addNewVote(newVoteDto);

        httpHeadersConfiguration.getHeaders().set("NewVoteID", String.valueOf(newVoteDtoId));

        return new ResponseEntity<>(httpHeadersConfiguration.getHeaders(), HttpStatus.OK);
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
                                                                                  @Positive(message = "User ID must be a Positive number!")
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
                                                                                    @Positive(message = "User ID must be a Positive number!")
                                                                                    int contentID,
                                                                                    @RequestParam("votedContentType")
                                                                                    @NotBlank(message = "Content Type cannot be Blank!")
                                                                                    @NotEmpty(message = "Content Type cannot be Empty!")
                                                                                    @NotNull(message = "Content Type cannot be null!")
                                                                                    String votedContentType) {

        return ResponseEntity.ok(voteService.getAllDownVotesByContentIDAndType(contentID, votedContentType));
    }
}
