package erkamber.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "votes")
@Getter
@Setter
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int voteID;

    @Column(name = "content_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int votedContentID;

    @Column(name = "user_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int userID;

    @Column(name = "votedContentType", length = 10, unique = false, updatable = false, insertable = true, nullable = false)
    private String votedContentType;

    @Column(name = "is_upvote", unique = false, updatable = true, insertable = true, nullable = false)
    private boolean isUpVote;

    public Vote() {
    }

    public Vote(int voteID, int votedContentID, int userID, String votedContentType, boolean isUpVote) {
        this.voteID = voteID;
        this.votedContentID = votedContentID;
        this.userID = userID;
        this.votedContentType = votedContentType;
        this.isUpVote = isUpVote;
    }
}
