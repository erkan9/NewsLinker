package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {

    @Positive(message = "Subscribe ID must be Positive number")
    private int subscribeID;

    @Positive(message = "Subscriber ID must be Positive number")
    private int subscriberID;

    @Positive(message = "Subscribe to person's ID must be Positive number")
    private int subscribedToID;
}
