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
    
        
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
