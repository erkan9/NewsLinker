package erkamber.controllers;

import erkamber.dtos.SubscribeDetailedDto;
import erkamber.dtos.SubscribeDto;
import erkamber.services.interfaces.SubscribeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://newslinker-backend.onrender.com"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class SubscribeController {

    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> addNewSubscription(@Valid @RequestBody SubscribeDto subscribeDto) {

        int newSubscriptionID = subscribeService.addNewSubscription(subscribeDto);

        return ResponseEntity.created(URI.create("api/v1/subscribe/" + newSubscriptionID)).build();
    }

    @GetMapping("/subscribe")
    public ResponseEntity<List<SubscribeDetailedDto>> getAllSubscriptions() {

        return ResponseEntity.ok(subscribeService.getAllSubscriptions());
    }

    @GetMapping(value = "/subscribe", params = {"subscriberId"})
    public ResponseEntity<List<SubscribeDetailedDto>> getAllSubscriptionsOfSubscriber(@RequestParam("subscriberId")
                                                                                      @Positive(message = "Subscriber ID must be a Positive number!")
                                                                                      int subscriberID) {

        return ResponseEntity.ok(subscribeService.getSubscriptionsBySubscriberID(subscriberID));
    }

    @GetMapping(value = "/subscribe", params = {"reporterId"})
    public ResponseEntity<List<SubscribeDetailedDto>> getAllSubscriptionsToSubscribed(@RequestParam("reporterId")
                                                                                      @Positive(message = "Reporter ID must be a Positive number!")
                                                                                      int reporterID) {

        return ResponseEntity.ok(subscribeService.getSubscriptionsByReporterID(reporterID));
    }

    @GetMapping(value = "/subscribers", params = {"reporterId"})
    public ResponseEntity<Integer> getNumberOfSubscribersOfReporter(@RequestParam("reporterId")
                                                                    @Positive(message = "Reporter ID must be a Positive number!")
                                                                    int reporterID) {

        return ResponseEntity.ok(subscribeService.getNumberOfSubscribersOfReporter(reporterID));
    }

    @GetMapping(value = "/subscribers", params = {"userId"})
    public ResponseEntity<Integer> getSubscriptionsOfUser(@RequestParam("userId")
                                                          @Positive(message = "User ID must be a Positive number!")
                                                          int userID) {

        return ResponseEntity.ok(subscribeService.getNumberOfSubscriptionsOfUser(userID));
    }

    @GetMapping(value = "/subscribe", params = {"subscriberId", "reporterId"})
    public ResponseEntity<SubscribeDetailedDto> getSubscriptionBySubscriberIDAndSubscribedToID(@RequestParam("subscriberId")
                                                                                               @Positive(message = "Subscriber ID must be a Positive number!")
                                                                                               int subscriberID,
                                                                                               @RequestParam("reporterId")
                                                                                               @Positive(message = "Reporter ID must be a Positive number!")
                                                                                               int reporterID) {

        return ResponseEntity.ok(subscribeService.getSubscriptionBySubscriberIDAndReporterID(subscriberID, reporterID));
    }

    @DeleteMapping(value = "/subscribe", params = {"subscriberId"})
    public ResponseEntity<Integer> deleteSubscriptionsOfSubscriberID(@RequestParam("subscriberId")
                                                                     @Positive(message = "Subscriber ID must be a Positive number!")
                                                                     int subscriberID) {

        return ResponseEntity.ok(subscribeService.deleteSubscriptionsOfSubscriberID(subscriberID));
    }

    @DeleteMapping(value = "/subscribe", params = {"reporterId"})
    public ResponseEntity<Integer> deleteSubscriptionsBySubscribedToID(@RequestParam("reporterId")
                                                                       @Positive(message = "Reporter ID must be a Positive number!")
                                                                       int reporterID) {

        return ResponseEntity.ok(subscribeService.deleteSubscriptionsByReporterID(reporterID));
    }

    @DeleteMapping(value = "/subscribe", params = {"subscriberId", "reporterId"})
    public void deleteSpecificSubscription(@RequestParam("subscriberId")
                                           @Positive(message = "Subscriber ID must be a Positive number!")
                                           int subscriberID,
                                           @RequestParam("reporterId")
                                           @Positive(message = "Reporter ID must be a Positive number!")
                                           int reporterID) {

        subscribeService.deleteSpecificSubscription(subscriberID, reporterID);
    }
}
