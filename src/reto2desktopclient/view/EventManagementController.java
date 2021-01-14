/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.EventManagerFactory;
import reto2desktopclient.model.Event;

/**
 *  TODO: Remove id column
 *        Validate item not col or row in table
 * @author Martin Angulo
 */
public class EventManagementController {
    private static final Logger LOGGER = Logger.getLogger(EventManagementController.class.getName());
    
    @FXML
    private Stage stage;
    
    @FXML
    private TableView<ObservableList<StringProperty>> tblEvents = new TableView<>();
    @FXML
    private TableColumn<String, Integer> colID;
    @FXML
    private TableColumn<String, String> colDate;
    @FXML
    private TableColumn<String, Double> colPrice;
    @FXML
    private TableColumn<String, String> colDescription;
    @FXML
    private TableColumn<String, String> colMusicGenre;
    @FXML
    private TableColumn<String, String> colPlace;
    
    /**
     * Initializes the scene and its components
     *
     * @param root
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Event Management");
        stage.setResizable(false);
        stage.show();
        
        List<Event> events = EventManagerFactory.getEventManager().getAllEvents(new GenericType<List<Event>>(){});
        for(Event e : events)
            System.out.println(e.getName());
        
        LOGGER.log(Level.INFO, "Successfully switched to Event Management window.");
    }
    
    /**
     * Removes an event.
     */
    @FXML
    private void handleButtonRemoveEvent() {
        LOGGER.log(Level.INFO, "Removing event.");
    }

    /**
     * Validates input and adds an event.
     */
    @FXML
    private void handleButtonAddEvent() {
        LOGGER.log(Level.INFO, "Adding event.");
    }
    
    /**
     * Switches the scene from Event Management to AdminMainMenu.
     */
    @FXML
    private void handleButtonBack() {
        try {
            LOGGER.log(Level.INFO, "Redirecting to AdminMainMenu window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/AdminMainMenu.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            AdminMainMenuController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to AdminMainMenu window: {0}", ex.getMessage());
        }
    }
    
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void getData(){}
}
