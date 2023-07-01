package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "views")
@Getter
@Setter
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int viewID;

    @Column(name = "view_creation_date", unique = false, updatable = false, insertable = true, nullable = false)
    private LocalDate viewCreationDate;

    @Column(name = "view_news_id", unique = false, updatable = true, insertable = true, nullable = false)
    private int viewNewsID;

    @Column(name = "view_user_id", unique = false, updatable = true, insertable = true, nullable = true)
    private int viewUserID;

    public View() {
    }

    public View(int viewID, LocalDate viewCreationDate, int viewNewsID) {
        this.viewID = viewID;
        this.viewCreationDate = viewCreationDate;
        this.viewNewsID = viewNewsID;
    }

    public View(int viewID, LocalDate viewCreationDate, int viewNewsID, int viewUserID) {
        this.viewID = viewID;
        this.viewCreationDate = viewCreationDate;
        this.viewNewsID = viewNewsID;
        this.viewUserID = viewUserID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        View view = (View) o;
        return viewID == view.viewID && viewNewsID == view.viewNewsID && viewUserID == view.viewUserID &&
                Objects.equals(viewCreationDate, view.viewCreationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewID, viewCreationDate, viewNewsID, viewUserID);
    }
}
