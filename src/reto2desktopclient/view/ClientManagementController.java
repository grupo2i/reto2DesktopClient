package reto2desktopclient.view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Aitor Fidalgo
 */
public class ClientManagementController {
    
    @FXML
    private Stage stage;
    @FXML
    private AdminMenuController adminMenuController;
   
    
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Client Management");
        stage.setResizable(false);
        adminMenuController.setStage(stage);
        stage.show();
    }
    
    private void switchToClubManagementWindow() {
        try {
            //LOGGER.log(Level.INFO, "Redirecting to ClientManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/ClubManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            ClubManagementController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            //LOGGER.log(Level.SEVERE, "Could not switch to ClientManagement window: {0}", ex.getMessage());
        }
      
    }
    
        
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
