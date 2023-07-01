package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "news_tags")
@Getter
@Setter
public class NewsTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_tag_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int newsTagID;

    @Column(name = "news_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int newsID;

    @Column(name = "tag_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int tagID;

    public NewsTag() {
    }

    public NewsTag(int newsTagID, int newsID, int tagID) {
        this.newsTagID = newsTagID;
        this.newsID = newsID;
        this.tagID = tagID;
    }
}
