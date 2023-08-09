package erkamber.services.implementations;

import erkamber.dtos.VoteDto;
import erkamber.entities.Vote;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.VoteMapper;
import erkamber.repositories.VoteRepository;
import erkamber.services.interfaces.VoteService;
import erkamber.validations.VoteValidation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final VoteValidation voteValidation;

    private final CommentServiceImpl commentService;

    @Lazy
    private final NewsServiceImpl newsService;

    public VoteServiceImpl(VoteRepository voteRepository, VoteMapper voteMapper,
                           VoteValidation voteValidation, CommentServiceImpl commentService, NewsServiceImpl newsService) {

        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
        this.voteValidation = voteValidation;
        this.commentService = commentService;
        this.newsService = newsService;
    }

    @Override
    public int addNewVote(VoteDto voteDto) {

        isVotedContentTypeCorrect(voteDto.getVotedContentType());

        Vote pastVote = hasVotedBefore(voteDto.getUserID(), voteDto.getVotedContentID(), voteDto.getVotedContentType());

        if (pastVote == null) {

            return handleNewVote(voteDto);

        } else {

            return handleNotNewVote(pastVote, voteDto);
        }
    }

    @Override
    public void deleteVotesUserID(int userID) {

        List<Vote> voteListByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    @Override
    public void deleteAllVotesByContentTypeAndUserID(String votedContentType, int userID) {

        isVotedContentTypeCorrect(votedContentType);

        List<Vote> voteListByUserID = voteRepository.getVoteByUserID(userID);

        voteListByUserID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    @Override
    public void deleteAllVotesByContentTypeAndVotedContentID(String votedContentType, int votedContentID) {

        isVotedContentTypeCorrect(votedContentType);

        List<Vote> voteListByUserID = voteRepository.getVoteByVotedContentID(votedContentID);

        voteListByUserID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    private int handleNotNewVote(Vote pastVote, VoteDto voteDto) {

        if (pastVote.isUpVote() == voteDto.isUpVote()) {

            if (pastVote.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.updateCommentVoteByRemovingVote(voteDto.getVotedContentID(), voteDto.isUpVote());

            } else if (pastVote.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.updateNewsVoteByRemovingVote(voteDto.getVotedContentID(), voteDto.isUpVote());
            }

            voteRepository.deleteById(pastVote.getVoteID());

        } else {

            if (pastVote.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.updateCommentVoteBySwappingVotes(voteDto.getVotedContentID(), voteDto.isUpVote());


            } else if (pastVote.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.updateNewsVoteBySwappingVotes(voteDto.getVotedContentID(), voteDto.isUpVote());
            }

            voteRepository.deleteById(pastVote.getVoteID());

            voteRepository.save(voteMapper.mapVoteDtoToVote(voteDto));

            return voteDto.getVoteID();
        }

        return 0;
    }

    private int handleNewVote(VoteDto voteDto) {

        Vote newVote = voteMapper.mapVoteDtoToVote(voteDto);

        if (voteDto.isUpVote()) {

            if (voteDto.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.addCommentUpVote(voteDto.getVotedContentID());

            } else if (voteDto.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.addNewsNewUpVote(voteDto.getVotedContentID());
            }

        } else {

            if (voteDto.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.addCommentDownVote(voteDto.getVotedContentID());

            } else if (voteDto.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.addNewsNewDownVote(voteDto.getVotedContentID());
            }


        }

        voteRepository.save(newVote);

        return newVote.getVoteID();
    }

    @Override
    public List<VoteDto> getAllUpVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllUpVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        validateVoteList(listOfAllUpVotesByContentID);

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotesByContentID);
    }

    @Override
    public List<VoteDto> getAllDownVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllDownVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        validateVoteList(listOfAllDownVotesByContentID);

        listOfAllDownVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllDownVotesByContentID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotesByContentID);
    }

    @Override
    public List<VoteDto> getAllUpVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(listOfAllVotesByUserID);

        listOfAllVotesByUserID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    @Override
    public List<VoteDto> getAllDownVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(listOfAllVotesByUserID);

        listOfAllVotesByUserID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    @Override
    public List<VoteDto> getAllVotes() {

        List<Vote> listOfAllVotes = voteRepository.findAll();

        validateVoteList(listOfAllVotes);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotes);
    }

    @Override
    public List<VoteDto> getAllUpVotes() {

        List<Vote> listOfAllUpVotes = voteRepository.findAll();

        validateVoteList(listOfAllUpVotes);

        listOfAllUpVotes.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotes);
    }

    @Override
    public List<VoteDto> getAllDownVotes() {

        List<Vote> listOfAllDownVotes = voteRepository.findAll();

        validateVoteList(listOfAllDownVotes);

        listOfAllDownVotes.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotes);
    }

    private void isVotedContentTypeCorrect(String votedContentType) {

        if (!voteValidation.isContentTypeValid(votedContentType)) {
            throw new InvalidInputException("Invalid, content type. Not Voted a comment or news!");
        }
    }

    private void validateVoteList(List<Vote> userList) {

        if (voteValidation.isVoteListEmpty(userList)) {

            throw new ResourceNotFoundException("Votes not Found", "Vote");
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
