package erkamber.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tags_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int tagsID;

    @Column(name = "tag_name", length = 40, unique = true, updatable = true, insertable = true, nullable = false)
    private String tagName;

    public Tag() {
    }

    public Tag(int tagsID, String tagName) {
        this.tagsID = tagsID;
        this.tagName = tagName;
    }
}
