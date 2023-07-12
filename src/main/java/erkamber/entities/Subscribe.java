package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subscribe")
@Getter
@Setter
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int subscribeID;

    @Column(name = "subscriber_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int subscriberID;

    @Column(name = "subscribed_to_id", unique = false, updatable = false, insertable = true, nullable = false)
    private int subscribedToID;

    public Subscribe() {
    }

    public Subscribe(int subscribeID, int subscriberID, int subscribedToID) {
        this.subscribeID = subscribeID;
        this.subscriberID = subscriberID;
        this.subscribedToID = subscribedToID;
    }
}
