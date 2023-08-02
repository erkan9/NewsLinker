package erkamber.mappers;

import erkamber.dtos.SubscribeDetailedDto;
import erkamber.dtos.SubscribeDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Subscribe;
import org.springframework.stereotype.Component;

@Component
public class SubscribeMapper {

    public SubscribeDto mapSubscribeToSubscribeDto(Subscribe subscribe) {

        return new SubscribeDto(subscribe.getSubscribeID(), subscribe.getSubscriberID(), subscribe.getReporterID());
    }

    public Subscribe mapSubscribeDtoToSubscribe(SubscribeDto subscribeDto) {

        return new Subscribe(subscribeDto.getSubscribeID(), subscribeDto.getSubscriberID(), subscribeDto.getReporterID());
    }

    public SubscribeDetailedDto mapToSubscribeDetailedDto(int subscribeID, UserDto subscriberDto, UserDto subscribedToDto) {

        return new SubscribeDetailedDto(subscribeID, subscriberDto, subscribedToDto);
    }
}
