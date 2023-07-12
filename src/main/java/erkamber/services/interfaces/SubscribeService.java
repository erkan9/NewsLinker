package erkamber.services.interfaces;

import erkamber.dtos.SubscribeDetailedDto;
import erkamber.dtos.SubscribeDto;

import java.util.List;

public interface SubscribeService {

    int addNewSubscription(SubscribeDto newSubscribeDto);

    int getNumberOfSubscribersOfReporter(int reporterID);

    int getNumberOfSubscriptionsOfUser(int userID);

    SubscribeDetailedDto getSubscriptionBySubscriberIDAndSubscribedToID(int subscriberID, int subscribedToID);

    List<SubscribeDetailedDto> getAllSubscriptions();

    List<SubscribeDetailedDto> getSubscriptionsBySubscriberID(int subscriberID);

    List<SubscribeDetailedDto> getSubscriptionsBySubscribedToID(int subscribedToID);

    int deleteSubscriptionsOfSubscriberID(int subscriberID);

    int deleteSubscriptionsBySubscribedToID(int subscribedToID);

    void deleteSpecificSubscription(int subscriberID, int subscribedToID);
}
