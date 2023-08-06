package erkamber.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
@Setter
public class ViewDto {

    @PositiveOrZero
    private int viewID;


    private LocalDate viewCreationDate;

    @Positive(message = "News ID must be Positive number")
    private int viewNewsID;

    @Positive(message = "User ID must be Positive number")
    private int viewUserID;

    public ViewDto() {
    }

    public ViewDto(int viewNewsID, int viewUserID) {
        this.viewNewsID = viewNewsID;
        this.viewUserID = viewUserID;
    }

    public ViewDto(LocalDate viewCreationDate, int viewNewsID, int viewUserID) {
        this.viewCreationDate = viewCreationDate;
        this.viewNewsID = viewNewsID;
        this.viewUserID = viewUserID;
    }

    public ViewDto(int viewID, LocalDate viewCreationDate, int viewNewsID, int viewUserID) {
        this.viewID = viewID;
        this.viewCreationDate = viewCreationDate;
        this.viewNewsID = viewNewsID;
        this.viewUserID = viewUserID;
    }
}
