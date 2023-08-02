package erkamber.dtos;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class SubscribeDto {

    @PositiveOrZero(message = "Subscribe ID must be Positive number")
    private int subscribeID;

    @Positive(message = "Subscriber ID must be Positive number")
    private int subscriberID;

    @Positive(message = "Subscribe to person's ID must be Positive number")
    private int reporterID;

    public SubscribeDto() {
    }

    public SubscribeDto(int subscriberID, int reporterID) {
        this.subscriberID = subscriberID;
        this.reporterID = reporterID;
    }

    public SubscribeDto(int subscribeID, int subscriberID, int reporterID) {
        this.subscribeID = subscribeID;
        this.subscriberID = subscriberID;
        this.reporterID = reporterID;
    }
}
