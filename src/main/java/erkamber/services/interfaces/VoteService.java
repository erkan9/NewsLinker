package erkamber.services.interfaces;

import erkamber.dtos.VoteDto;

import java.util.List;

public interface VoteService {

    int addNewVote(VoteDto newVoteDto);

    void deleteVotesUserID(int userID);

    void deleteAllVotesByContentTypeAndUserID(String votedContentType, int userID);

    void deleteAllVotesByContentTypeAndVotedContentID(String votedContentType, int votedContentID);

    List<VoteDto> getAllUpVotesByContentIDAndType(int votedContentID, String votedContentType);

    List<VoteDto> getAllDownVotesByContentIDAndType(int votedContentID, String votedContentType);

    List<VoteDto> getAllUpVotesByUserID(int userID);

    List<VoteDto> getAllDownVotesByUserID(int userID);

    List<VoteDto> getAllVotes();

    List<VoteDto> getAllUpVotes();

    List<VoteDto> getAllDownVotes();
}
