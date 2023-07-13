package erkamber.repositories;

import erkamber.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    List<Vote> getVoteByUserID(int userID);

    List<Vote> getVoteByVotedContentID(int contentID);
}
