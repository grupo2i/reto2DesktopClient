package reto2desktopclient.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Controller for the AdminMenu window.
 *
 * @author aitor
 */
public class AdminMenuController {

    private static final Logger LOGGER = Logger.getLogger(AdminMenuController.class.getName());

    @FXML
    private Stage stage;
    
    
    /**
     * Handles the onAction event over the Manage Clients menuItem by switching
     * to Client Management window.
     * 
     * @param event Event that is being handled.
     */
    @FXML
    private void handleManageClients(ActionEvent event) {
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
     * Handles the onAction event over the Manage Clubs menuItem by switching
     * to Club Management window.
     * 
     * @param event Event that is being handled.
     */
    @FXML
    private void handleManageClubs(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ClubManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/ClubManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClubManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ClubManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Club Management window due to an"
                    + " unexpected error, please try later.");
        }
    }

    /**
     * Handles the onAction event over the Manage Artist menuItem by switching
     * to Artist Management window.
     * 
     * @param event Event that is being handled.
     */
    @FXML
    private void handleManageArtists(ActionEvent event) {
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
     * Handles the onAction event over the Manage Events menuItem by switching
     * to Event Management window.
     * 
     * @param event Event that is being handled.
     */
    @FXML
    private void handleManageEvents(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to EventManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/EventManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            EventManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to EventManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Event Management window due to an"
                    + " unexpected error, please try later.");
        }
    }

    /**
     * Handles the onAction event over the Log Out menuItem by switching
     * to LogIn window.
     * 
     * @param event Event that is being handled.
     */
    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to LogIn window.");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/reto2desktopclient/view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to LogIn window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Log In window due to an"
                    + " unexpected error, please try later.");
        }
    }

    /**
     * @return The stage.
     */
    public Stage getStage() {
        return stage;
    }
    /**
     * Sets the stage attribute with the given stage.
     * @param stage The given stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
