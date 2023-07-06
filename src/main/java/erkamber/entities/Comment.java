package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int commendID;

    @Column(name = "author_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int commentAuthorID;

    @Column(name = "news_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int commentNewsID;

    @Column(name = "comment_content", length = 300, unique = false, updatable = true, insertable = true, nullable = false)
    private String commentContent;

    @Column(name = "up_votes", unique = false, updatable = true, insertable = true, nullable = false)
    private int commentUpVotes;

    @Column(name = "down_votes", unique = false, updatable = true, insertable = true, nullable = false)
    private int commentDownVotes;

    private LocalDate creationDate;

    public Comment() {
    }

    public Comment(int commendID, int commentAuthorID, int commentNewsID, String commentContent,
                   int commentUpVotes, int commentDownVotes, LocalDate creationDate) {

        this.commendID = commendID;
        this.commentAuthorID = commentAuthorID;
        this.commentNewsID = commentNewsID;
        this.commentContent = commentContent;
        this.commentUpVotes = commentUpVotes;
        this.commentDownVotes = commentDownVotes;
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commendID == comment.commendID && commentAuthorID == comment.commentAuthorID &&
                commentUpVotes == comment.commentUpVotes && commentDownVotes == comment.commentDownVotes &&
                Objects.equals(commentContent, comment.commentContent) &&
                Objects.equals(creationDate, comment.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commendID, commentAuthorID, commentContent, commentUpVotes, commentDownVotes, creationDate);
    }
}
