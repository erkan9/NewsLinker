package erkamber.mappers;

import erkamber.dtos.VoteDto;
import erkamber.entities.Vote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VoteMapper {

    public VoteDto mapVoteToVoteDto(Vote vote) {

        return new VoteDto(vote.getVoteID(), vote.getVotedContentID(), vote.getUserID(), vote.getVotedContentType(), vote.isUpVote());
    }

    public Vote mapVoteDtoToVote(VoteDto voteDto) {

        return new Vote(voteDto.getVoteID(), voteDto.getVotedContentID(), voteDto.getUserID(), voteDto.getVotedContentType(), voteDto.isUpVote());
    }


    public List<VoteDto> mapListOfVoteToMVoteDto(List<Vote> listOfVotes) {

        return listOfVotes.stream().map(this::mapVoteToVoteDto).collect(Collectors.toList());
    }
}
