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
    private int tagID;

    @Column(name = "tag_name", length = 40, unique = true, updatable = true, insertable = true, nullable = false)
    private String tagName;

    public Tag() {
    }

    public Tag(int tagID, String tagName) {
        this.tagID = tagID;
        this.tagName = tagName;
    }
}
