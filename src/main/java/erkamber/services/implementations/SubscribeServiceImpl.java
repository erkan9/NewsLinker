package erkamber.services.implementations;

import erkamber.dtos.SubscribeDetailedDto;
import erkamber.dtos.SubscribeDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Subscribe;
import erkamber.entities.User;
import erkamber.exceptions.NotReporterException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.SubscribeMapper;
import erkamber.mappers.UserMapper;
import erkamber.repositories.SubscribeRepository;
import erkamber.repositories.UserRepository;
import erkamber.services.interfaces.SubscribeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final SubscribeMapper subscribeMapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public SubscribeServiceImpl(SubscribeRepository subscribeRepository, SubscribeMapper subscribeMapper,
                                UserRepository userRepository, UserMapper userMapper) {
        this.subscribeRepository = subscribeRepository;
        this.subscribeMapper = subscribeMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Adds a new subscription to a user reporter.
     *
     * @param newSubscribeDto The {@link SubscribeDto} containing subscription details.
     * @return The ID of the newly added subscription.
     * @throws NotReporterException If the user being subscribed to is not a reporter.
     */
    @Override
    public int addNewSubscription(SubscribeDto newSubscribeDto) {

        isSubscribedToUserReporter(newSubscribeDto.getReporterID());

        Subscribe subscribe = subscribeMapper.mapSubscribeDtoToSubscribe(newSubscribeDto);

        subscribeRepository.save(subscribe);

        return subscribe.getSubscribeID();
    }

    /**
     * Retrieves the number of subscribers for a user reporter.
     *
     * @param reporterID The ID of the user reporter.
     * @return The number of subscribers.
     * @throws NotReporterException If the user being subscribed to is not a reporter.
     */
    @Override
    public int getNumberOfSubscribersOfReporter(int reporterID) {

        isSubscribedToUserReporter(reporterID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeByReporterID(reporterID);

        return subscribersOfReporter.size();
    }

    /**
     * Retrieves the number of subscriptions for a user.
     *
     * @param userID The ID of the user.
     * @return The number of subscriptions.
     */
    @Override
    public int getNumberOfSubscriptionsOfUser(int userID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(userID);

        return subscriptionsOfUser.size();
    }

    /**
     * Retrieves a detailed subscription by subscriber ID and reporter ID.
     *
     * @param subscriberID The ID of the subscriber.
     * @param reporterID   The ID of the reporter.
     * @return The detailed subscription DTO.
     * @throws ResourceNotFoundException If the subscription is not found.
     * @throws NotReporterException      If the user being subscribed to is not a reporter.
     */
    @Override
    public SubscribeDetailedDto getSubscriptionBySubscriberIDAndReporterID(int subscriberID, int reporterID) {

        isSubscribedToUserReporter(reporterID);

        Optional<Subscribe> searchedSubscribeOptional = subscribeRepository.findSubscribeBySubscriberIDAndReporterID(subscriberID, reporterID);

        Subscribe searchedSubscription = searchedSubscribeOptional.orElseThrow(() ->
                new ResourceNotFoundException("Subscription not Found", "Subscription"));

        UserDto subscriberUser = getUserDtoObjectFromUserID(subscriberID);

        UserDto subscribedToUser = getUserDtoObjectFromUserID(reporterID);

        return subscribeMapper.mapToSubscribeDetailedDto(searchedSubscription.getSubscribeID(), subscriberUser, subscribedToUser);
    }

    /**
     * Retrieves a list of all detailed subscriptions.
     *
     * @return A list of {@link SubscribeDetailedDto} objects representing the detailed subscriptions.
     * @throws NotReporterException If a user being subscribed to is not a reporter.
     */
    @Override
    public List<SubscribeDetailedDto> getAllSubscriptions() {

        List<Subscribe> listOfAllSubscriptions = subscribeRepository.findAll();

        return convertListOfSubscribeToSubscribeDetailedDto(listOfAllSubscriptions);
    }

    /**
     * Retrieves a list of detailed subscriptions for a subscriber user.
     *
     * @param subscriberID The ID of the subscriber user.
     * @return A list of {@link SubscribeDetailedDto} objects representing the detailed subscriptions.
     */
    @Override
    public List<SubscribeDetailedDto> getSubscriptionsBySubscriberID(int subscriberID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(subscriberID);

        return convertListOfSubscribeToSubscribeDetailedDto(subscriptionsOfUser);
    }

    /**
     * Retrieves a list of detailed subscriptions for a reporter user.
     *
     * @param reporterID The ID of the reporter user.
     * @return A list of {@link SubscribeDetailedDto} objects representing the detailed subscriptions.
     * @throws NotReporterException If the reporter user is not a reporter.
     */
    @Override
    public List<SubscribeDetailedDto> getSubscriptionsByReporterID(int reporterID) {

        isSubscribedToUserReporter(reporterID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeByReporterID(reporterID);

        return convertListOfSubscribeToSubscribeDetailedDto(subscribersOfReporter);
    }

    /**
     * Deletes all subscriptions of a subscriber user.
     *
     * @param subscriberID The ID of the subscriber user.
     * @return The number of subscriptions deleted.
     */
    @Override
    public int deleteSubscriptionsOfSubscriberID(int subscriberID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(subscriberID);

        subscribeRepository.deleteAll(subscriptionsOfUser);

        return subscriptionsOfUser.size();
    }

    /**
     * Deletes all subscriptions of a reporter
     *
     * @param reporterID The ID of the reporter user.
     * @return The number of subscriptions deleted.
     * @throws NotReporterException If the reporter user is not a reporter.
     */
    @Override
    public int deleteSubscriptionsByReporterID(int reporterID) {

        isSubscribedToUserReporter(reporterID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeByReporterID(reporterID);

        subscribeRepository.deleteAll(subscribersOfReporter);

        return subscribersOfReporter.size();
    }

    /**
     * Deletes a specific subscription.
     *
     * @param subscriberID   The ID of the subscriber.
     * @param subscribedToID The ID of the user being subscribed to.
     * @throws NotReporterException      If the user being subscribed to is not a reporter.
     * @throws ResourceNotFoundException If the subscription is not found.
     */
    @Override
    public void deleteSpecificSubscription(int subscriberID, int subscribedToID) {

        isSubscribedToUserReporter(subscribedToID);

        Optional<Subscribe> searchedSubscribeOptional = subscribeRepository.findSubscribeBySubscriberIDAndReporterID(subscriberID, subscribedToID);

        Subscribe searchedSubscription = searchedSubscribeOptional.orElseThrow(() ->
                new ResourceNotFoundException("Subscription not Found", "Subscription"));

        subscribeRepository.delete(searchedSubscription);
    }

    private void isSubscribedToUserReporter(int subscribedToUserID) {

        UserDto subscribedToUser = getUserDtoObjectFromUserID(subscribedToUserID);

        if (!subscribedToUser.isUserReporter()) {

            throw new NotReporterException("Cannot Subscribe to a normal User. ");
        }
    }

    /**
     * Retrieves a {@link UserDto} object from a given user ID.
     *
     * @param userID The ID of the user.
     * @return The {@link UserDto} representing the user.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    private UserDto getUserDtoObjectFromUserID(int userID) {

        Optional<User> user = userRepository.findById(userID);

        User searchedUser = user.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userID, "User"));

        return userMapper.mapUserToUserDto(searchedUser);
    }

    /**
     * Converts a list of {@link Subscribe} objects to a list of {@link SubscribeDetailedDto} objects.
     *
     * @param listOfSubscriptions The list of {@link Subscribe} objects to convert.
     * @return The list of {@link SubscribeDetailedDto} objects.
     */
    private List<SubscribeDetailedDto> convertListOfSubscribeToSubscribeDetailedDto(List<Subscribe> listOfSubscriptions) {

        List<SubscribeDetailedDto> listOfAllSubscribeDetailedDto = new ArrayList<>();

        for (Subscribe subscribe : listOfSubscriptions) {

            UserDto subscriberUser = getUserDtoObjectFromUserID(subscribe.getSubscriberID());

            UserDto subscribedToUser = getUserDtoObjectFromUserID(subscribe.getReporterID());

            SubscribeDetailedDto subscribeDetailedDto = new SubscribeDetailedDto(subscribe.getSubscribeID(), subscriberUser, subscribedToUser);

            listOfAllSubscribeDetailedDto.add(subscribeDetailedDto);
        }

        return listOfAllSubscribeDetailedDto;
    }
}
