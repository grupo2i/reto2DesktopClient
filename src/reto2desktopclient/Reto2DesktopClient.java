package reto2desktopclient;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import reto2desktopclient.view.LogInController;

/**
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public class Reto2DesktopClient extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/LogIn.fxml"));
        Parent root = (Parent) loader.load();
        LogInController controller = (loader.getController());
        primaryStage.setResizable(Boolean.FALSE);
        controller.setStage(primaryStage);
        controller.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
