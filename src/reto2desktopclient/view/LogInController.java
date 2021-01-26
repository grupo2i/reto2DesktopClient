package reto2desktopclient.view;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ProcessingException;
import reto2desktopclient.client.UserManagerFactory;
import reto2desktopclient.exceptions.UnexpectedErrorException;
import reto2desktopclient.model.Artist;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.User;
import reto2desktopclient.security.PublicCrypt;

/**
 * Controls the SignIn window behaviour.
 *
 * @author Ander Vicente, Aitor Fidalgo
 */
public class LogInController {

    private static final Logger LOGGER = Logger.getLogger(LogInController.class.getName());
    
    @FXML
    private Stage stage;
    @FXML
    private Button btnAccept;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private Label lblErrorUsername;
    @FXML
    private Label lblErrorPassword;
    
    private boolean errorUsername = true;
    private boolean errorPassword = true;
    

    /**
     * Initializes the scene and its components
     *
     * @param root
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.setResizable(false);
        txtUsername.requestFocus();
        txtUsername.textProperty().addListener(this::textChangedUser);
        pwdPassword.textProperty().addListener(this::textChangedPassword);
        btnAccept.setDisable(true);
        lblErrorUsername.setVisible(false);
        lblErrorPassword.setVisible(false);
        btnAccept.setTooltip(
                new Tooltip("Click to send credentials "));
        btnAccept.setDefaultButton(true);
        stage.show();
        LOGGER.log(Level.INFO, "Successfully switched to Log In window.");
    }
    
    /**
     * Tries to log in, if introduced fields ok enters to app.
     *
     * @param event
     */
    @FXML
    private void handleButtonAccept(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Accept button pressed");
            //Getting userPrivilege attribute of the user trying to sign in
            //to avoid Unmarshalling error at the signIn method call.
            User user = UserManagerFactory.getUserManager().
                    getPrivilege(User.class, txtUsername.getText());
            String encodedPassword = PublicCrypt.encode(pwdPassword.getText());
            switch(user.getUserPrivilege()) {
                case ADMIN:
                    //Retrieving User from signIn method.
                    user = UserManagerFactory.getUserManager().signIn(
                            User.class, txtUsername.getText(), encodedPassword);
                    //Updating users last access...
                    user.setLastAccess(new Date());
                    UserManagerFactory.getUserManager().edit(user);
                    switchToClientManagementWindow();
                    break;
                case ARTIST:
                    //Retrieving Artist from signIn method.
                    Artist artist = UserManagerFactory.getUserManager().signIn(
                            Artist.class, txtUsername.getText(), encodedPassword);
                    //Updating artists last access...
                    artist.setLastAccess(new Date());
                    UserManagerFactory.getUserManager().edit(artist);
                    switchToArtistProfileWindow();
                    break;
                case CLUB:
                    //Retrieving Club from signIn method.
                    Club club = UserManagerFactory.getUserManager().signIn(
                            Club.class, txtUsername.getText(), encodedPassword);
                    //Updating clubs last access...
                    club.setLastAccess(new Date());
                    UserManagerFactory.getUserManager().edit(club);
                    switchToClubProfileWindow();
                    break;
                default:
                    showErroAlert("Invalid login, only Clubs, Artists and Administrators are allowed.");
                    break;
            }
        } catch(NotAuthorizedException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            showErroAlert("User not found, incorrect login or password.");
        } catch (UnexpectedErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            showErroAlert(ex.getMessage());
        } catch(ProcessingException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            showErroAlert("Could not process the request, please try later.");
        }
    }
    
    /**
     * Switches the scene from LogIn to AdminMainMenu.
     */
    private void switchToClientManagementWindow() {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ClientManagement window.");

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/ClientManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClientManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ClientManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Client Management window due to an"
                    + " unexpected error, please try later.");
        }
      
    }
    
    /**
     * Switches the scene from LogIn to ArtistManagement.
     */
    private void switchToArtistProfileWindow() {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ArtistManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/ArtistManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ArtistManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ArtistManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Artist Management window due to an"
                    + " unexpected error, please try later.");
        }
    }
    
    /**
     * Switches the scene from LogIn to ClubManagement.
     */
    private void switchToClubProfileWindow() {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ClubManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/ClubProfile.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClubProfileController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ArtistManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Artist Management window due to an"
                    + " unexpected error, please try later.");
        }
    }
    
    /**
     * Shows an error Alert window with the specified message.
     * 
     * @param errorMessage The specified message.
     */
    private void showErroAlert(String errorMessage) {
        LOGGER.log(Level.INFO, "Showing Alert window with error message...");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        errorAlert.show();
    }

    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     *
     * @param obs
     */
    private void textChangedPassword(Observable obs) {
        Integer pwdLenght = pwdPassword.getText().trim().length();
        if (pwdLenght < 4 || pwdLenght > 255) {
            errorPassword = true;
            lblErrorPassword.setVisible(true);
        } else {
            errorPassword = false;
            lblErrorPassword.setVisible(false);
        }
        //if typing and then erasing, reminder that field must be greater than 6
        if (pwdLenght < 4) {
            lblErrorPassword.setText("* Must be at least 4 characters");
        }
        //if typing and then erasing and exceeding max characters, reminding not to do it 
        if (pwdLenght > 255) {
            lblErrorPassword.setText("* Must be less than 255 characters");
        }

        testInputErrors();
    }

    /**
     * Tests if there is any error on every txtUsername text propertie change.
     *
     * @param obs
     */
    private void textChangedUser(Observable obs) {
        Integer usLenght = txtUsername.getText().trim().length();
        //if username =0 or <255= error
        if (usLenght == 0 || usLenght > 255) {
            errorUsername = true;
            lblErrorUsername.setVisible(true);
        } else {
            errorUsername = false;
            lblErrorUsername.setVisible(false);
        }
        //if typing and then erasing, reminder that field must not be empty
        if (usLenght == 0) {
            lblErrorUsername.setText("* Field must not be empty");
        }
        //if typing and then erasing and exceeding max characters, reminding not to do it      
        if (usLenght > 255) {
            lblErrorUsername.setText("* Must be less than 255 characters");
        }

        testInputErrors();
    }

    /**
     * Checks if there is any input error and disables btnAccept if so.
     */
    private void testInputErrors() {
        if (errorPassword || errorUsername) {
            btnAccept.setDisable(true);
        } else {
            btnAccept.setDisable(false);
        }
    }
    
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}