package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDetailedDto {

    @Positive(message = "Subscribe ID must be Positive number")
    private int subscribeID;


    private UserDto subscriberID;


    private UserDto subscribedToID;
}
