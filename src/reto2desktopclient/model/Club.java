package reto2desktopclient.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin Angulo
 */
@XmlRootElement
public class Club extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    public Club(String location, String phoneNum) {
        this.location = new SimpleStringProperty(location);
        this.phoneNum = new SimpleStringProperty(phoneNum);

    }

    public Club() {
        this.location = new SimpleStringProperty();
        this.phoneNum = new SimpleStringProperty();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public SimpleStringProperty phoneNumProperty() {
        return phoneNum;
    }

    private final SimpleStringProperty location;
    private final SimpleStringProperty phoneNum;

    private Set<Event> events;

    /**
     * @return the location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location.set(location);
    }

    /**
     * @return the phoneNum
     */
    public String getPhoneNum() {
        return phoneNum.get();
    }

    /**
     * @param phoneNum the phoneNum to set
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }

    /**
     * @return the events
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Club[ id=" + id + " ]";
    }
}
