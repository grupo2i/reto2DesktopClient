package reto2desktopclient.model;

import java.io.Serializable;

/**
 * Compound id for Rates form by a User and an Events id.
 * @see Rating
 * @see User
 * @see Event
 * @author Aitor Fidalgo
 */
public class RatingId implements Serializable{
    /**
     * Id of the User that made the Rating.
     */
    private Integer clientId;
    /**
     * Id of the Event that has been rated.
     */
    private Integer eventId;

    /**
     * Empty constructor.
     */
    public RatingId(){
    }
    /**
     * Constructor that sets the values sent as parameters.
     * @param clientId Users id.
     * @param eventId Events id.
     */
    public RatingId(Integer clientId, Integer eventId){
        this.clientId = clientId;
        this.eventId = eventId;
    }

    /**
     * @return The id of the User that made the rating.
     */
    public Integer getClientId() {
        return clientId;
    }
    /**
     * Sets the id of the User.
     * @param userId Id of the User.
     */
    public void setClientId(Integer userId) {
        this.clientId = userId;
    }
    /**
     * @return The id of the Eventthat has beenrated.
     */
    public Integer getEventId() {
        return eventId;
    }
    /**
     * Sets the id of the Event.
     * @param eventId Id of the Event.
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
