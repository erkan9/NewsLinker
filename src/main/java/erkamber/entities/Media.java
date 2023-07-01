package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "medias")
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int mediaID;

    @Column(name = "media_news_id", unique = false, updatable = true, insertable = true, nullable = false)
    private int mediaNewsID;

    @Column(name = "media_string", length = 255, unique = false, updatable = true, insertable = true, nullable = false)
    private String mediaString;

    public Media() {
    }

    public Media(int mediaID, int mediaNewsID, String mediaString) {
        this.mediaID = mediaID;
        this.mediaNewsID = mediaNewsID;
        this.mediaString = mediaString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return mediaID == media.mediaID && mediaNewsID == media.mediaNewsID && mediaString == media.mediaString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaID, mediaNewsID, mediaString);
    }
}
