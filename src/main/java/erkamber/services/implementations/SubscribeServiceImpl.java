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

    @Override
    public int addNewSubscription(SubscribeDto newSubscribeDto) {

        isSubscribedToUserReporter(newSubscribeDto.getSubscribedToID());

        Subscribe subscribe = subscribeMapper.mapSubscribeDtoToSubscribe(newSubscribeDto);

        subscribeRepository.save(subscribe);

        return subscribe.getSubscribeID();
    }

    @Override
    public int getNumberOfSubscribersOfReporter(int reporterID) {

        isSubscribedToUserReporter(reporterID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeBySubscribedToID(reporterID);

        return subscribersOfReporter.size();
    }

    @Override
    public int getNumberOfSubscriptionsOfUser(int userID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(userID);

        return subscriptionsOfUser.size();
    }

    @Override
    public SubscribeDetailedDto getSubscriptionBySubscriberIDAndSubscribedToID(int subscriberID, int subscribedToID) {

        isSubscribedToUserReporter(subscribedToID);

        Optional<Subscribe> searchedSubscribeOptional = subscribeRepository.findSubscribeBySubscriberIDAndSubscribedToID(subscriberID, subscribedToID);

        Subscribe searchedSubscription = searchedSubscribeOptional.orElseThrow(() ->
                new ResourceNotFoundException("Subscription not Found", "Subscription"));

        UserDto subscriberUser = getUserDtoObjectFromUserID(subscriberID);

        UserDto subscribedToUser = getUserDtoObjectFromUserID(subscribedToID);

        return subscribeMapper.mapToSubscribeDetailedDto(searchedSubscription.getSubscribeID(), subscriberUser, subscribedToUser);
    }

    @Override
    public List<SubscribeDetailedDto> getAllSubscriptions() {

        List<Subscribe> listOfAllSubscriptions = subscribeRepository.findAll();

        return convertListOfSubscribeToSubscribeDetailedDto(listOfAllSubscriptions);
    }

    @Override
    public List<SubscribeDetailedDto> getSubscriptionsBySubscriberID(int subscriberID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(subscriberID);

        return convertListOfSubscribeToSubscribeDetailedDto(subscriptionsOfUser);
    }

    @Override
    public List<SubscribeDetailedDto> getSubscriptionsBySubscribedToID(int subscribedToID) {

        isSubscribedToUserReporter(subscribedToID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeBySubscribedToID(subscribedToID);

        return convertListOfSubscribeToSubscribeDetailedDto(subscribersOfReporter);
    }

    @Override
    public int deleteSubscriptionsOfSubscriberID(int subscriberID) {

        List<Subscribe> subscriptionsOfUser = subscribeRepository.findSubscribeBySubscriberID(subscriberID);

        subscribeRepository.deleteAll(subscriptionsOfUser);

        return subscriptionsOfUser.size();
    }

    @Override
    public int deleteSubscriptionsBySubscribedToID(int subscribedToID) {

        isSubscribedToUserReporter(subscribedToID);

        List<Subscribe> subscribersOfReporter = subscribeRepository.findSubscribeBySubscribedToID(subscribedToID);

        subscribeRepository.deleteAll(subscribersOfReporter);

        return subscribersOfReporter.size();
    }

    @Override
    public void deleteSpecificSubscription(int subscriberID, int subscribedToID) {

        isSubscribedToUserReporter(subscribedToID);

        Optional<Subscribe> searchedSubscribeOptional = subscribeRepository.findSubscribeBySubscriberIDAndSubscribedToID(subscriberID, subscribedToID);

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

    private UserDto getUserDtoObjectFromUserID(int userID) {

        Optional<User> user = userRepository.findById(userID);

        User searchedUser = user.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userID, "User"));

        return userMapper.mapUserToUserDto(searchedUser);
    }

    private List<SubscribeDetailedDto> convertListOfSubscribeToSubscribeDetailedDto(List<Subscribe> listOfSubscriptions) {

        List<SubscribeDetailedDto> listOfAllSubscribeDetailedDto = new ArrayList<>();

        for (Subscribe subscribe : listOfSubscriptions) {

            UserDto subscriberUser = getUserDtoObjectFromUserID(subscribe.getSubscriberID());

            UserDto subscribedToUser = getUserDtoObjectFromUserID(subscribe.getSubscribedToID());

            SubscribeDetailedDto subscribeDetailedDto = new SubscribeDetailedDto(subscribe.getSubscribeID(), subscriberUser, subscribedToUser);

            listOfAllSubscribeDetailedDto.add(subscribeDetailedDto);
        }

        return listOfAllSubscribeDetailedDto;
    }
}
