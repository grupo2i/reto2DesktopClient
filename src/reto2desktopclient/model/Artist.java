package reto2desktopclient.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
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
    private String musicGenre;
    private Set<Event> events;
    private String tblLogin;
    private String tbEmail;
    private String tblName;
    private LocalDate tblLastaccess;
    private String tblStatus;

    public Artist(){}
    
    /**
     *
     * @param tblLogin
     * @param tbEmail
     * @param tblName
     * @param tblLastaccess
     * @param musicGenre
     * @param tblStatus
     */
    public Artist(String tblLogin, String tbEmail, String tblName, LocalDate tblLastaccess, String musicGenre, String tblStatus) {
        this.tblLogin = tblLogin;
        this.tbEmail = tbEmail;
        this.tblName = tblName;
        this.tblLastaccess = tblLastaccess;
        this.musicGenre = musicGenre;
        this.tblStatus = tblStatus;
    }

    /**
     *
     * @return
     */
    public String getTblLogin() {
        return tblLogin;
    }

    /**
     *
     * @param tblLogin
     */
    public void setTblLogin(String tblLogin) {
        this.tblLogin = tblLogin;
    }

    /**
     *
     * @return
     */
    public String getTbEmail() {
        return tbEmail;
    }

    /**
     *
     * @param tbEmail
     */
    public void setTbEmail(String tbEmail) {
        this.tbEmail = tbEmail;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public LocalDate getTblLastaccess() {
        return tblLastaccess;
    }

    public void setTblLastaccess(LocalDate tblLastaccess) {
        this.tblLastaccess = tblLastaccess;
    }

    public String getTblStatus() {
        return tblStatus;
    }

    /**
     *
     * @param tblStatus
     */
    public void setTblStatus(String tblStatus) {
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

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     *
     * @param musicGenre
     */
    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }

    /**
     *
     * @return
     */
    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    /**
     *
     * @return
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     *
     * @return
     */
    public String getMusicGenre() {
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
