package reto2desktopclient.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Superclass of all type of users, contains common attributes.
 * 
 * @author Aitor Fidalgo
 */
@XmlRootElement
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new User and initializes its properties to the specified values.
     * 
     * @param login Specified value for login property.
     * @param email Specified value for email property.
     * @param fullName Specified value for fullName property.
     * @param userStatus Specified value for userStatus property.
     * @param lastAccess Specified value for lastAccess property.
     */
    public User(String login,
                String email,
                String fullName,
                UserStatus userStatus,
                Date lastAccess) {
        this.login = new SimpleStringProperty(login);
        this.email = new SimpleStringProperty(email);
        this.fullName = new SimpleStringProperty(fullName);
        this.userStatus = new SimpleObjectProperty<>(userStatus);
        this.lastAccess = new SimpleObjectProperty<>(lastAccess);
    }
    
    /**
     * Contructs a new User.
     */
    public User() {
        this.login = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.userStatus = new SimpleObjectProperty<>();
        this.lastAccess = new SimpleObjectProperty<>();
    }
    
    public SimpleStringProperty loginProperty() {
        return login;
    }
    
    public SimpleStringProperty emailProperty() {
        return email;
    }
    
    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }
    
    public SimpleObjectProperty<UserStatus> userStatusProperty() {
        return userStatus;
    }
    
    public SimpleObjectProperty<Date> lastAccessProperty() {
        return lastAccess;
    }
    
    /**
     * Used to identify Users.
     */
    protected Integer id;
    
    /**
     * Unique name of the User in the system.
     */
    private final SimpleStringProperty login;
    /**
     * Email of the User.
     */
    private final SimpleStringProperty email;
    /**
     * Full and real name of the User.
     */
    private final SimpleStringProperty fullName;
    /**
     * Brief description the Users writes about themselves.
     */
    private String biography;
    /**
     * Two possible value enum that defines the Users status.
     */
    private final SimpleObjectProperty<UserStatus> userStatus;
    /**
     * Enum that defines the type of User.
     */
    private UserPrivilege userPrivilege;
    /**
     * Credential of the User.
     */
    private String password;
    /**
     * Specifies the last time the User loged in into the system.
     */
    private final SimpleObjectProperty<Date> lastAccess;
    /**
     * Specifies the last time the User chaged their password.
     */
    private Date lastPasswordChange;
    /**
     * Name of the profile image of the User.
     */
    private String profileImage;
    
    /**
     * Ratings of an event made by a User.
     * 
     * The relation was supposed to be between Client and Rating but due to an
     * Hibernate bug it can't be done. See more <a href="https://discourse.hibernate.org/t/embededid-containing-a-foreign-key-of-an-entity-with-inheritance/2334">here</a>
     */
    private Set<Rating> ratings;
    
    
    /**
     * @return Id of the User.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the value of the id.
     * @param id The value.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * @return The login of the User.
     */
    public String getLogin() {
        return login.get();
    }
    /**
     * Sets the value of the login.
     * @param login The value.
     */
    public void setLogin(String login) {
        this.login.set(login);
    }
    /**
     * @return The email of he User.
     */
    public String getEmail() {
        return email.get();
    }
    /**
     * Sets the value of the email.
     * @param email The value.
     */
    public void setEmail(String email) {
        this.email.set(email);
    }
    /**
     * @return The full name of the User.
     */
    public String getFullName() {
        return fullName.get();
    }
    /**
     * Sets the value of the full name.
     * @param fullName The value.
     */
    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }
    /**
     * @return The biography of the User.
     */
    public String getBiography() {
        return biography;
    }
    /**
     * Sets the value of the biography.
     * @param biography The value.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }
    /**
     * @return The status of the User.
     */
    public UserStatus getUserStatus() {
        return userStatus.get();
    }
    /**
     * Sets the value of the user status.
     * @param userStatus The value.
     */
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus.set(userStatus);
    }
    /**
     * @return The user privilege.
     */
    public UserPrivilege getUserPrivilege() {
        return userPrivilege;
    }
    /**
     * Sets the value of the user privilege.
     * @param userPrivilege The value.
     */
    public void setUserPrivilege(UserPrivilege userPrivilege) {
        this.userPrivilege = userPrivilege;
    }
    /**
     * @return The password of the User.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the value of the password.
     * @param password The value.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return The last access of the User.
     */
    public Date getLastAccess() {
        return lastAccess.get();
    }
    /**
     * Sets the value of the last access.
     * @param lastAccess The value.
     */
    public void setLastAccess(Date lastAccess) {
        this.lastAccess.set(lastAccess);
    }
    /**
     * @return The last password change of the User.
     */
    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }
    /**
     * Sets the value of the last password change.
     * @param lastPasswordChange The value.
     */
    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }
    /**
     * @return The profile image name of the User.
     */
    public String getProfileImage() {
        return profileImage;
    }
    /**
     * Sets the value of the profile image name.
     * @param profileImage The value.
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    /**
     * @return The ratings made by the User.
     */
    public Set<Rating> getRatings() {
        return ratings;
    }
    /**
     * Sets the value of the ratings.
     * @param ratings The value.
     */
    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
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
        if (!(object instanceof User)) {
            return false;
        }
         User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.User[ id=" + id + " ]";
    }
    
}
