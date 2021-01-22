package reto2desktopclient.model;

import java.io.Serializable;
import java.time.LocalDate;
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
    private SimpleStringProperty musicGenre;
    private Set<Event> events;
    private SimpleStringProperty tblLogin;
    private SimpleStringProperty tbEmail;
    private SimpleStringProperty tblName;
    private LocalDate tblLastaccess;
    private SimpleStringProperty tblStatus;

    public Artist(String tblLogin,
            String tbEmail,
            String tblName,
            LocalDate tblLastaccess,
            String musicGenre,
            String tblStatus) {
        this.tblLogin = new SimpleStringProperty(tblLogin);
        this.tbEmail = new SimpleStringProperty(tbEmail);
        this.tblName = new SimpleStringProperty(tblName);
        this.tblLastaccess = tblLastaccess;
        this.musicGenre = new SimpleStringProperty(musicGenre);
        this.tblStatus = new SimpleStringProperty(tblStatus);
    }

    public Artist() {
        this.tblLogin = new SimpleStringProperty();
        this.tbEmail = new SimpleStringProperty();
        this.tblName = new SimpleStringProperty();
        this.tblLastaccess = tblLastaccess;
        this.musicGenre = new SimpleStringProperty();
        this.tblStatus = new SimpleStringProperty();
    }

    public LocalDate getTblLastaccess() {
        return tblLastaccess;
    }

    public void setTblLastaccess(LocalDate tblLastaccess) {
        this.tblLastaccess = tblLastaccess;
    }

    public SimpleStringProperty getTblLogin() {
        return tblLogin;
    }

    public void setTblLogin(SimpleStringProperty tblLogin) {
        this.tblLogin = tblLogin;
    }

    public SimpleStringProperty getTbEmail() {
        return tbEmail;
    }

    public void setTbEmail(SimpleStringProperty tbEmail) {
        this.tbEmail = tbEmail;
    }

    public SimpleStringProperty getTblName() {
        return tblName;
    }

    public void setTblName(SimpleStringProperty tblName) {
        this.tblName = tblName;
    }

    public SimpleStringProperty getTblStatus() {
        return tblStatus;
    }

    public void setTblStatus(SimpleStringProperty tblStatus) {
        this.tblStatus = tblStatus;
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
    public SimpleStringProperty getMusicGenre() {
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
