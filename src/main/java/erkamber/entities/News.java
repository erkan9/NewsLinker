package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "news")
@Getter
@Setter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int newsID;

    @Column(name = "user_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int userID;

    @Column(name = "news_topic", length = 30, unique = false, updatable = true, insertable = true, nullable = false)
    private String newsTitle;

    @Column(name = "news_content", length = 255, unique = false, updatable = true, insertable = true, nullable = false)
    private String newsContent;

    @Column(name = "news_up_votes", updatable = true, insertable = true, nullable = false)
    private int newsUpVotes;

    @Column(name = "news_down_votes", updatable = true, insertable = true, nullable = false)
    private int newsDownVotes;

    @Column(name = "news_creation_date", unique = false, updatable = false, insertable = true, nullable = false)
    private LocalDateTime newsCreationDate;

    public News() {
    }

    public News(int newsID, int userID, String newsTitle, String newsContent, int newsUpVotes, int newsDownVotes,
                LocalDateTime newsCreationDate) {

        this.newsID = newsID;
        this.userID = userID;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsUpVotes = newsUpVotes;
        this.newsDownVotes = newsDownVotes;
        this.newsCreationDate = newsCreationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return newsID == news.newsID && newsUpVotes == news.newsUpVotes && newsDownVotes == news.newsDownVotes &&
                Objects.equals(newsTitle, news.newsTitle) && Objects.equals(newsContent, news.newsContent) &&
                Objects.equals(newsCreationDate, news.newsCreationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsID, newsTitle, newsContent, newsUpVotes, newsDownVotes, newsCreationDate);
    }
}
