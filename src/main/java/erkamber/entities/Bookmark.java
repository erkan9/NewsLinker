package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookmarkID;

    @Column(name = "user_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int userID;

    @Column(name = "news_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int newsID;

    public Bookmark() {
    }

    public Bookmark(int bookmarkID, int userID, int newsID) {
        this.bookmarkID = bookmarkID;
        this.userID = userID;
        this.newsID = newsID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bookmark bookmark = (Bookmark) o;
        return bookmarkID == bookmark.bookmarkID && userID == bookmark.userID && newsID == bookmark.newsID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookmarkID, userID, newsID);
    }
}
