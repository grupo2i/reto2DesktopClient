package reto2desktopclient.view;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClientManagerFactory;
import reto2desktopclient.model.Client;

/**
 * Controller for the ClientManagement window.
 * 
 * @author Aitor Fidalgo
 */
public class ClientManagementController {
    
    private static final Logger LOGGER = Logger
            .getLogger(ClientManagementController.class.getName());
    
    @FXML
    private Stage stage;
    @FXML
    private AdminMenuController adminMenuController;
    
    @FXML
    private Label lblInputError;
    @FXML
    private Button btnNewClient;
    @FXML
    private Button btnSeeEvents;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem menuItemNewClient;
    @FXML
    private MenuItem menuItemSeeEvents;
    
    @FXML
    private TableView tableClients;
    @FXML
    private TableColumn tableColLogin;
    @FXML
    private TableColumn tableColEmail;
    @FXML
    private TableColumn tableColFullName;
    @FXML
    private TableColumn tableColLastAccess;
    @FXML
    private TableColumn tableColStatus;
    
    private ObservableList clientsData;
   
    
    /**
     * Initializes the stage of the window.
     * 
     * @param root Parent of all nodes in the scene graph.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Client Management");
        stage.setResizable(false);
        //Setting stage showing event.
        stage.setOnShowing(this::handleWindowShowing);
        //Setting the stage of adminMenuController to allow window navigation.
        adminMenuController.setStage(stage);
        stage.hide();
        stage.show();
    }
    
    /**
     * Handles the showing event of the stage by initializing window elements.
     * 
     * @param event Showing event of the stage.
     */
    private void handleWindowShowing(WindowEvent event) {
        try {
            LOGGER.log(Level.INFO, "Starting method handleWindowShowing on {0}",
                    ClientManagementController.class.getName());
            //New Client button has the focus.
            btnNewClient.requestFocus();
            //See Events button and menu item are disabled.
            btnSeeEvents.setDisable(true);
            menuItemSeeEvents.setDisable(true);
            //Hiding user input error label.
            lblInputError.setVisible(false);

            //Stablishing cell value factories on table columns...
            tableColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
            tableColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            tableColFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            tableColStatus.setCellValueFactory(new PropertyValueFactory<>("userStatus"));
            tableColLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));

            //Creating a Client observable List with all registered clients.
            clientsData = FXCollections.observableList(ClientManagerFactory
                    .getClientManager().getAllClients(new GenericType<List<Client>>(){}));
            //Setting the table model to the observable list above.
            tableClients.setItems(clientsData);
            //Setting table selection listener.
            tableClients.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleClientsTableSelectionChange);
        } catch(ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        
    }
    
    private void handleClientsTableSelectionChange(ObservableValue observable,
                                Object oldVaue, Object newValue) {
        if(newValue != null) { //A row of the table is selected.
            //Enable See Events button and menu item.
            btnSeeEvents.setDisable(false);
            menuItemSeeEvents.setDisable(false);
        } else { //There isn't any row selected.
            //Disable See Events button and menu item.
            btnSeeEvents.setDisable(true);
            menuItemSeeEvents.setDisable(true);
        }
    }
    
    private void handleNewClient() {
        
    }
    
    private void handleSeeEvents() {
        
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
