package erkamber.services.implementations;

import erkamber.dtos.VoteDto;
import erkamber.entities.Vote;
import erkamber.exceptions.InvalidInputException;
import erkamber.mappers.VoteMapper;
import erkamber.repositories.VoteRepository;
import erkamber.services.interfaces.VoteService;
import erkamber.validations.VoteContentTypeValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final VoteContentTypeValidation voteContentTypeValidation;

    private final CommentServiceImpl commentService;

    public VoteServiceImpl(VoteRepository voteRepository, VoteMapper voteMapper,
                           VoteContentTypeValidation voteContentTypeValidation, CommentServiceImpl commentService) {

        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
        this.voteContentTypeValidation = voteContentTypeValidation;
        this.commentService = commentService;
    }

    @Override
    public void addNewVote(VoteDto voteDto) {

        isVotedContentTypeCorrect(voteDto.getVotedContentType());

        Vote pastVote = hasVotedBefore(voteDto.getUserID(), voteDto.getVotedContentID(), voteDto.getVotedContentType());

        if (pastVote == null) {

            handleNewVote(voteDto);

        } else {

            handleNotNewVote(pastVote, voteDto);
        }
    }

    private void handleNotNewVote(Vote pastVote, VoteDto voteDto) {

        if (pastVote.isUpVote() == voteDto.isUpVote()) {

            commentService.updateCommentVoteByRemovingVote(voteDto.getVotedContentID(), voteDto.isUpVote());

            voteRepository.deleteById(pastVote.getVoteID());

        } else {

            commentService.updateCommentVoteBySwappingVotes(voteDto.getVotedContentID(), voteDto.isUpVote());

            voteRepository.deleteById(pastVote.getVoteID());

            voteRepository.save(voteMapper.mapVoteDtoToVote(voteDto));
        }
    }

    private void handleNewVote(VoteDto voteDto) {

        Vote newVote = voteMapper.mapVoteDtoToVote(voteDto);

        voteRepository.save(newVote);

        if (voteDto.isUpVote()) {

            commentService.addCommentUpVote(voteDto.getVotedContentID());

        } else {

            commentService.addCommentDownVote(voteDto.getVotedContentID());
        }
    }

    @Override
    public List<VoteDto> getAllUpVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllUpVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotesByContentID);
    }

    @Override
    public List<VoteDto> getAllDownVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllDownVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        listOfAllDownVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllDownVotesByContentID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotesByContentID);
    }

    @Override
    public List<VoteDto> getAllUpVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        listOfAllVotesByUserID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    @Override
    public List<VoteDto> getAllDownVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        listOfAllVotesByUserID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    @Override
    public List<VoteDto> getAllVotes() {

        List<Vote> listOfAllVotes = voteRepository.findAll();

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotes);
    }

    @Override
    public List<VoteDto> getAllUpVotes() {

        List<Vote> listOfAllUpVotes = voteRepository.findAll();

        listOfAllUpVotes.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotes);
    }

    @Override
    public List<VoteDto> getAllDownVotes() {

        List<Vote> listOfAllDownVotes = voteRepository.findAll();

        listOfAllDownVotes.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotes);
    }

    private void isVotedContentTypeCorrect(String votedContentType) {

        if (voteContentTypeValidation.isContentTypeValid(votedContentType)) {
            throw new InvalidInputException("Invalid, content type. Not Voted a comment or news!");
        }
    }

    private Vote hasVotedBefore(int userID, int votedContentID, String contentType) {

        List<Vote> votesByUser = voteRepository.getVoteByUserID(userID);

        Optional<Vote> foundVote = votesByUser.stream()
                .filter(vote -> vote.getVotedContentID() == votedContentID && vote.getVotedContentType().equalsIgnoreCase(contentType))
                .findFirst();

        return foundVote.orElse(null);
    }
}
