package erkamber.services.interfaces;

import erkamber.dtos.SubscribeDetailedDto;
import erkamber.dtos.SubscribeDto;

import java.util.List;

public interface SubscribeService {

    int addNewSubscription(SubscribeDto newSubscribeDto);

    int getNumberOfSubscribersOfReporter(int reporterID);

    int getNumberOfSubscriptionsOfUser(int userID);

    int deleteSubscriptionsOfSubscriberID(int subscriberID);

    int deleteSubscriptionsByReporterID(int reporterID);

    void deleteSpecificSubscription(int subscriberID, int subscribedToID);

    SubscribeDetailedDto getSubscriptionBySubscriberIDAndReporterID(int subscriberID, int reporterID);

    List<SubscribeDetailedDto> getAllSubscriptions();

    List<SubscribeDetailedDto> getSubscriptionsBySubscriberID(int subscriberID);

    List<SubscribeDetailedDto> getSubscriptionsByReporterID(int reporterID);
}
