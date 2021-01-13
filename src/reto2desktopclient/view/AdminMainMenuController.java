package reto2desktopclient.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Aitor Fidalgo
 */
public class AdminMainMenuController {
    
    private static final Logger LOGGER = Logger.getLogger(AdminMainMenuController.class.getName());
    
    @FXML
    private Stage stage;
    
    
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Admin Menu");
        stage.setResizable(false);
        stage.show();
        LOGGER.log(Level.INFO, "Successfully switched to Log In window.");
    }
    
    /**
     * Switches the scene from AdminMainMenu to LogIn.
     * 
     * @param event ActionEvent created by Log Out button.
     */
    @FXML
    private void handleButtonLogOut(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to LogIn window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to LogIn window: {0}", ex.getMessage());
        }
    }
    
    /**
     * Switches the scene from AdminMainMenu to ClientManagement.
     * 
     * @param event ActionEvent created by Manage Clients button.
     */
    @FXML
    private void handleButtonManageClients(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ClientManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/ClientManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClientManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ClientManagement window: {0}", ex.getMessage());
        }
    }
    
    /**
     * Switches the scene from AdminMainMenu to ClubManagement.
     * 
     * @param event ActionEvent created by Manage Clubs button.
     */
    @FXML
    private void handleButtonManageClubs(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ClubManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/ClubManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClubManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ClubManagement window: {0}", ex.getMessage());
        }
    }
    
    /**
     * Switches the scene from AdminMainMenu to ArtistManagement.
     * 
     * @param event ActionEvent created by Manage Artists button.
     */
    @FXML
    private void handleButtonManageArtists(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to ArtistManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/ArtistManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ArtistManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to ArtistManagement window: {0}", ex.getMessage());
        }
    }
    
    /**
     * Switches the scene from AdminMainMenu to EventManagement.
     * 
     * @param event ActionEvent created by Manage Events button.
     */
    @FXML
    private void handleButtonManageEvents(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to EventManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/EventManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            EventManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to EventManagement window: {0}", ex.getMessage());
        }
    }
    
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
