package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
public class Feedback {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int feedbackID;

    @Column(name = "author_id", unique = false, updatable = true, insertable = true, nullable = false)
    private int authorID;

    @Column(name = "feedback_content", length = 400, unique = false, updatable = true, insertable = true, nullable = false)
    private String feedbackContent;

    public Feedback() {
    }

    public Feedback(int feedbackID, int authorID, String feedbackContent) {
        this.feedbackID = feedbackID;
        this.authorID = authorID;
        this.feedbackContent = feedbackContent;
    }
}
