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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Adds a new vote based on the provided VoteDto.
     *
     * @param newVoteDto The VoteDto containing information about the new vote.
     * @return The result of handling the new vote, which may include updating an existing vote.
     * @throws IllegalArgumentException If the voted content type is incorrect.
     */
    @Override
    @Transactional
    public int addNewVote(VoteDto newVoteDto) {

        isVotedContentTypeCorrect(newVoteDto.getVotedContentType());

        Vote pastVote = hasVotedBefore(newVoteDto.getUserID(), newVoteDto.getVotedContentID(), newVoteDto.getVotedContentType());

        if (pastVote == null) {

            return handleNewVote(newVoteDto);

        } else {

            return handleNotNewVote(pastVote, newVoteDto);
        }
    }

    /**
     * Deletes all votes associated with a specific user ID.
     *
     * @param userID The ID of the user whose votes will be deleted.
     * @throws ResourceNotFoundException If the user has no votes or does not exist.
     */
    @Override
    public void deleteVotesUserID(int userID) {

        List<Vote> voteListByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    /**
     * Deletes all votes of a specific content type associated with a given user ID.
     *
     * @param votedContentType The type of content for which votes will be deleted.
     * @param userID           The ID of the user whose votes will be deleted.
     * @throws IllegalArgumentException  If the voted content type is incorrect.
     * @throws ResourceNotFoundException If the user has no votes or does not exist.
     */
    @Override
    public void deleteAllVotesByContentTypeAndUserID(String votedContentType, int userID) {

        isVotedContentTypeCorrect(votedContentType);

        List<Vote> voteListByUserID = voteRepository.getVoteByUserID(userID);

        voteListByUserID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    /**
     * Deletes all votes of a specific content type associated with a given voted content ID.
     *
     * @param votedContentType The type of content for which votes will be deleted.
     * @param votedContentID   The ID of the content for which votes will be deleted.
     * @throws IllegalArgumentException  If the voted content type is incorrect.
     * @throws ResourceNotFoundException If there are no votes for the specified content or the content does not exist.
     */
    @Override
    public void deleteAllVotesByContentTypeAndVotedContentID(String votedContentType, int votedContentID) {

        isVotedContentTypeCorrect(votedContentType);

        List<Vote> voteListByUserID = voteRepository.getVoteByVotedContentID(votedContentID);

        voteListByUserID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        validateVoteList(voteListByUserID);

        voteRepository.deleteAll(voteListByUserID);
    }

    /**
     * Handles updating an existing vote based on the provided pastVote and voteDto.
     *
     * @param pastVote   The previous vote associated with the content.
     * @param newVoteDto The VoteDto containing information about the updated vote.
     * @return The ID of the updated vote, if applicable.
     */
    @Transactional
    int handleNotNewVote(Vote pastVote, VoteDto newVoteDto) {

        if (pastVote.isUpVote() == newVoteDto.isUpVote()) {

            if (pastVote.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.updateCommentVoteByRemovingVote(newVoteDto.getVotedContentID(), newVoteDto.isUpVote());

            } else if (pastVote.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.updateNewsVoteByRemovingVote(newVoteDto.getVotedContentID(), newVoteDto.isUpVote());
            }

            voteRepository.deleteById(pastVote.getVoteID());

        } else {

            if (pastVote.getVotedContentType().equalsIgnoreCase("comment")) {

                commentService.updateCommentVoteBySwappingVotes(newVoteDto.getVotedContentID(), newVoteDto.isUpVote());

            } else if (pastVote.getVotedContentType().equalsIgnoreCase("news")) {

                newsService.updateNewsVoteBySwappingVotes(newVoteDto.getVotedContentID(), newVoteDto.isUpVote());
            }

            pastVote.setUpVote(newVoteDto.isUpVote());

            voteRepository.save(pastVote);

            return newVoteDto.getVoteID();
        }

        return 0;
    }

    /**
     * Handles adding a new vote based on the provided VoteDto.
     *
     * @param voteDto The VoteDto containing information about the new vote.
     * @return The ID of the newly added vote.
     */
    @Transactional
    int handleNewVote(VoteDto voteDto) {

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

    /**
     * Retrieves a list of VoteDtos representing all upvotes for a specific content based on its ID and type.
     *
     * @param votedContentID   The ID of the content for which upvotes will be retrieved.
     * @param votedContentType The type of content for which upvotes will be retrieved.
     * @return A list of VoteDtos representing all upvotes for the specified content.
     * @throws ResourceNotFoundException If there are no upvotes for the specified content or the content does not exist.
     */
    @Override
    public List<VoteDto> getAllUpVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllUpVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        validateVoteList(listOfAllUpVotesByContentID);

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllUpVotesByContentID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotesByContentID);
    }

    /**
     * Retrieves a list of VoteDtos representing all downvotes for a specific content based on its ID and type.
     *
     * @param votedContentID   The ID of the content for which downvotes will be retrieved.
     * @param votedContentType The type of content for which downvotes will be retrieved.
     * @return A list of VoteDtos representing all downvotes for the specified content.
     * @throws ResourceNotFoundException If there are no downvotes for the specified content or the content does not exist.
     */
    @Override
    public List<VoteDto> getAllDownVotesByContentIDAndType(int votedContentID, String votedContentType) {

        List<Vote> listOfAllDownVotesByContentID = voteRepository.getVoteByVotedContentID(votedContentID);

        validateVoteList(listOfAllDownVotesByContentID);

        listOfAllDownVotesByContentID.removeIf(vote -> !vote.getVotedContentType().equalsIgnoreCase(votedContentType));

        listOfAllDownVotesByContentID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotesByContentID);
    }

    /**
     * Retrieves a list of VoteDtos representing all upvotes given by a specific user.
     *
     * @param userID The ID of the user for which upvotes will be retrieved.
     * @return A list of VoteDtos representing all upvotes given by the specified user.
     * @throws ResourceNotFoundException If the user has not given any upvotes or the user does not exist.
     */
    @Override
    public List<VoteDto> getAllUpVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(listOfAllVotesByUserID);

        listOfAllVotesByUserID.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    /**
     * Retrieves a list of VoteDtos representing all downvotes given by a specific user.
     *
     * @param userID The ID of the user for which downvotes will be retrieved.
     * @return A list of VoteDtos representing all downvotes given by the specified user.
     * @throws ResourceNotFoundException If the user has not given any downvotes or the user does not exist.
     */
    @Override
    public List<VoteDto> getAllDownVotesByUserID(int userID) {

        List<Vote> listOfAllVotesByUserID = voteRepository.getVoteByUserID(userID);

        validateVoteList(listOfAllVotesByUserID);

        listOfAllVotesByUserID.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotesByUserID);
    }

    /**
     * Retrieves a list of all VoteDtos representing all votes.
     *
     * @return A list of VoteDtos representing all votes.
     * @throws ResourceNotFoundException If there are no votes.
     */
    @Override
    public List<VoteDto> getAllVotes() {

        List<Vote> listOfAllVotes = voteRepository.findAll();

        validateVoteList(listOfAllVotes);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllVotes);
    }

    /**
     * Retrieves a list of VoteDtos representing all upvotes.
     *
     * @return A list of VoteDtos representing all upvotes.
     * @throws ResourceNotFoundException If there are no upvotes.
     */
    @Override
    public List<VoteDto> getAllUpVotes() {

        List<Vote> listOfAllUpVotes = voteRepository.findAll();

        validateVoteList(listOfAllUpVotes);

        listOfAllUpVotes.removeIf(vote -> !vote.isUpVote());

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllUpVotes);
    }

    /**
     * Retrieves a list of VoteDtos representing all downvotes.
     *
     * @return A list of VoteDtos representing all downvotes.
     * @throws ResourceNotFoundException If there are no downvotes.
     */
    @Override
    public List<VoteDto> getAllDownVotes() {

        List<Vote> listOfAllDownVotes = voteRepository.findAll();

        validateVoteList(listOfAllDownVotes);

        listOfAllDownVotes.removeIf(Vote::isUpVote);

        return voteMapper.mapListOfVoteToMVoteDto(listOfAllDownVotes);
    }

    /**
     * Validates whether the voted content type is correct (i.e., "comment" or "news").
     *
     * @param votedContentType The content type to be validated.
     * @throws InvalidInputException If the voted content type is invalid.
     */
    private void isVotedContentTypeCorrect(String votedContentType) {

        if (!voteValidation.isContentTypeValid(votedContentType)) {
            throw new InvalidInputException("Invalid, content type. Not Voted a comment or news!");
        }
    }

    /**
     * Validates whether a list of votes is empty or not.
     *
     * @param userList The list of user to be validated.
     * @throws ResourceNotFoundException If the list of votes is empty.
     */
    private void validateVoteList(List<Vote> userList) {

        if (voteValidation.isVoteListEmpty(userList)) {

            throw new ResourceNotFoundException("Votes not Found", "Vote");
        }
    }

    /**
     * Checks if a user has voted for a specific content before.
     *
     * @param userID         The ID of the user to check for previous votes.
     * @param votedContentID The ID of the content to check for previous votes.
     * @param contentType    The type of content to check for previous votes.
     * @return The previous vote by the user for the specified content, if any; otherwise, null.
     */
    private Vote hasVotedBefore(int userID, int votedContentID, String contentType) {

        List<Vote> votesByUser = voteRepository.getVoteByUserID(userID);

        Optional<Vote> foundVote = votesByUser.stream()
                .filter(vote -> vote.getVotedContentID() == votedContentID && vote.getVotedContentType().equalsIgnoreCase(contentType))
                .findFirst();

        return foundVote.orElse(null);
    }
}
