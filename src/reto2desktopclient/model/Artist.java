package reto2desktopclient.model;

import java.io.Serializable;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class Artist extends from user
 *
 * @author Ander, Matteo
 */
@XmlRootElement
public class Artist extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<SocialNetwork> socialNetworks;
    private MusicGenre musicGenre;
    private Set<Event> events;

    public Artist(MusicGenre musicGenre) {

    }

    public Artist() {
    }

    /**
     *
     * @return
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSocialNetworks(Set<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    /**
     *
     * @return
     */
    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    /**
     * @return the events
     */
    public Set<Event> getEvents() {
        return events;
    }

    public void setMusicGenre(MusicGenre musicGenre) {
        this.musicGenre = musicGenre;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    

    /**
     * @param events the events to set
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     *
     * @return
     */
    public MusicGenre getMusicGenre() {
        return musicGenre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.artista[ id=" + id + " ]";
    }

}
