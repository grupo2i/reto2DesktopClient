package reto2desktopclient.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClientManager;
import reto2desktopclient.client.ClientManagerFactory;
import reto2desktopclient.exceptions.UnexpectedErrorException;
import reto2desktopclient.model.Client;
import reto2desktopclient.model.UserPrivilege;
import reto2desktopclient.model.UserStatus;
import reto2desktopclient.security.PublicCrypt;

/**
 * Controller for the ClientManagement window.
 *
 * @author Aitor Fidalgo
 */
public class ClientManagementController {

    /**
     * Logger used to leave traces.
     */
    private static final Logger LOGGER = Logger
            .getLogger(ClientManagementController.class.getName());
    /**
     * Contains all the profile images names.
     */
    private static final String[] PROFILE_IMAGES = {"badass_coffee_avatar",
        "crazy_ass_avocado_avatar", "crazy_ass_sheep_avatar", "donald_trump_avatar",
        "heisenberg_avatar", "high_af_harley_quinn_avatar", "marilyn_monroe_avatar",
        "normal_guy_avatar", "punky_avatar", "puritan_nun_avatar", "wasted_zombie_avatar"};
    /**
     * ClientManager used to make requests to the server.
     */
    private static final ClientManager CLIENT_MANAGER = ClientManagerFactory.getClientManager();

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
    private MenuItem menuItemSeeEvents;

    @FXML
    private TableView tableClients;
    @FXML
    private TableColumn<Client, String> tableColLogin;
    @FXML
    private TableColumn<Client, String> tableColEmail;
    @FXML
    private TableColumn<Client, String> tableColFullName;
    @FXML
    private TableColumn<Client, Date> tableColLastAccess;
    @FXML
    private TableColumn<Client, UserStatus> tableColStatus;

    private ObservableList clientsData;

    /**
     * Initializes the stage of the window.
     *
     * @param root Parent of all nodes in the scene graph.
     */
    public void initStage(Parent root) {
        LOGGER.log(Level.INFO, "Starting stage initialization for"
                + " Client Management window...");
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
            //Making table editable.
            tableClients.setEditable(true);

            LOGGER.log(Level.INFO, "Setting cell value factories on table columns...");
            //Setting cell value factories on table columns...
            tableColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
            tableColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            tableColFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            tableColStatus.setCellValueFactory(new PropertyValueFactory<>("userStatus"));
            tableColLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));

            LOGGER.log(Level.INFO, "Setting cell factories on table columns...");
            //Setting cell factory on table columns...
            tableColLogin.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColFullName.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColStatus.setCellFactory(ComboBoxTableCell.forTableColumn(
                    UserStatus.DISABLED, UserStatus.ENABLED));
            
            //Setting new format to the last access column.
            tableColLastAccess.setCellFactory(column -> {
                TableCell<Client, Date> cell = new TableCell<Client, Date>() {
                    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        empty = (item == null);
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        }
                        else {
                            setText(format.format(item));
                        }
                    }
                };
                return cell;
            });

            LOGGER.log(Level.INFO, "Setting table model...");
            //Creating a Client observable List with all registered clients.
            clientsData = FXCollections.observableList(CLIENT_MANAGER
                    .getAllClients(new GenericType<List<Client>>() {
            }));
            //Setting the table model to the observable list above.
            tableClients.setItems(clientsData);
            tableClients.refresh();
            //Setting table selection listener.
            tableClients.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleClientsTableSelectionChange);

            //Setting edit commit event handler to login column.
            tableColLogin.setOnEditCommit(
                (CellEditEvent<Client, String> t) -> {
                    try {
                        LOGGER.log(Level.INFO, "Handling Login column edit commit event...");
                        if (dataLengthIsValid(t.getNewValue())) {
                            Client newClient = t.getRowValue();
                            newClient.setLogin(t.getNewValue());
                            //Getting a list of all registered clients 
                            //to check the login is not registered already.
                            boolean loginIsValid = true;
                            ObservableList<Client> clients = FXCollections.observableList(
                                    CLIENT_MANAGER.getAllClients(new GenericType<List<Client>>() {
                            }));
                            //Checking the login is not registered already...
                            for(Client client:clients) {
                                //Checking that the login is not null to avoid NullPointerException.
                                //and new Client is not compared to itself.
                                if(client.getLogin() != null && !client.getId().equals(newClient.getId())) {
                                    if(client.getLogin().equalsIgnoreCase(newClient.getLogin())) {
                                        loginIsValid = false;
                                        //Showing login is already registered error message.
                                        lblInputError.setText("* Login is already registered.");
                                        lblInputError.setVisible(true);
                                        break;
                                    }
                                }
                            }
                            //If login is not already registered update the database
                            //with the new Client.
                            if(loginIsValid) {
                                LOGGER.log(Level.INFO, "Updating Clients login in the"
                                        + " database on Login column edit commit handling...");
                                //Hiding login is already registered error message.
                                lblInputError.setVisible(false);
                                //Updating newClient in the database.
                                CLIENT_MANAGER.edit(newClient);
                            } else {
                                //Getting the row number where the new client was.
                                int rowIndex = tableClients.getItems().indexOf(t.getRowValue());
                                //Getting new clients old value manually...
                                for(Client client:clients) {
                                    if(client.getId().equals(newClient.getId())) {
                                        newClient = clients.remove(clients.indexOf(client));
                                        break;
                                    }
                                }
                                //Resetting the items of the table and placing
                                //the new client where it was before.
                                tableClients.setItems(clients);
                                tableClients.getItems().add(rowIndex, newClient);
                                tableClients.getSelectionModel().select(rowIndex);
                            }
                        } else {
                            //Resetting old value if new value is not valid.
                            t.getRowValue().setLogin(t.getOldValue());
                            tableClients.refresh();
                        }
                    } catch(InternalServerErrorException ex) {
                        LOGGER.log(Level.SEVERE, ex.getMessage());
                        LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
                        showErroAlert(ex.getCause().getMessage());
                    }
                });
            //Setting edit commit event handler to email column.
            tableColEmail.setOnEditCommit(
                (CellEditEvent<Client, String> t) -> {
                    try {
                        LOGGER.log(Level.INFO, "Handling Email column edit commit event...");
                        if (dataLengthIsValid(t.getNewValue())
                        && emailPatternIsValid(t.getNewValue())) {
                            Client newClient = t.getRowValue();
                            newClient.setEmail(t.getNewValue());
                            boolean emailIsValid = true;
                            //Getting a list of all registered clients 
                            //to check the login is not registered already.
                            ObservableList<Client> clients = FXCollections.observableList(
                                    CLIENT_MANAGER.getAllClients(new GenericType<List<Client>>() {
                            }));
                            //Checking the email is not registered already...
                            for(Client client:clients) {
                                //Checking that the email is not null to avoid NullPointerException.
                                //and new Client is not compared to itself.
                                if(client.getEmail() != null && !client.getId().equals(newClient.getId())) {
                                    if(client.getEmail().equalsIgnoreCase(newClient.getEmail())) {
                                        emailIsValid = false;
                                        //Showing email is already registered error message.
                                        lblInputError.setText("* Email is already registered.");
                                        lblInputError.setVisible(true);
                                        break;
                                    }
                                }
                            }
                            //If email is not already registered update the database
                            //with the new Client.
                            if(emailIsValid) {
                                LOGGER.log(Level.INFO, "Updating Clients email in the"
                                        + " database on Email column edit commit handling...");
                                //Hiding login is already registered error message.
                                lblInputError.setVisible(false);
                                //Updating newClient in the database.
                                CLIENT_MANAGER.edit(newClient);
                            } else {
                                //Getting the row number where the new client was.
                                int rowIndex = tableClients.getItems().indexOf(t.getRowValue());
                                //Getting new clients old value manually...
                                for(Client client:clients) {
                                    if(client.getId().equals(newClient.getId())) {
                                        newClient = clients.remove(clients.indexOf(client));
                                        break;
                                    }
                                }
                                //Resetting the items of the table and placing
                                //the new client where it was before.
                                tableClients.setItems(clients);
                                tableClients.getItems().add(rowIndex, newClient);
                                tableClients.getSelectionModel().select(rowIndex);
                            }
                        } else {
                            //Resetting old value if new value is not valid.
                            t.getRowValue().setEmail(t.getOldValue());
                            tableClients.refresh();
                        }
                    } catch(InternalServerErrorException ex) {
                        LOGGER.log(Level.SEVERE, ex.getMessage());
                        LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
                        showErroAlert(ex.getCause().getMessage());
                    }
                });
            //Setting edit commit event handler to full name column.
            tableColFullName.setOnEditCommit(
                (CellEditEvent<Client, String> t) -> {
                    try {
                        LOGGER.log(Level.INFO, "Handling Full Name column edit commit event...");
                        if (dataLengthIsValid(t.getNewValue())) {
                            LOGGER.log(Level.INFO, "Updating Clients full name in the"
                                    + " database on Full Name column edit commit handling...");
                            //Updating newClient in the database.
                            Client client = t.getRowValue();
                            client.setFullName(t.getNewValue());
                            CLIENT_MANAGER.edit(client);
                            lblInputError.setVisible(false);
                        } else {
                            //Resetting old value if new value is not valid.
                            t.getRowValue().setFullName(t.getOldValue());
                            tableClients.refresh();
                        }
                    } catch(InternalServerErrorException ex) {
                        LOGGER.log(Level.SEVERE, ex.getMessage());
                        LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
                        showErroAlert(ex.getCause().getMessage());
                    }
                });
            //Setting edit commit event handler to user status column.
            tableColStatus.setOnEditCommit(
                (CellEditEvent<Client, UserStatus> t) -> {
                    try {
                        LOGGER.log(Level.INFO, "Handling Status column edit commit event...");
                        Client newClient = t.getRowValue();
                        newClient.setUserStatus(t.getNewValue());
                        //Updating newClient in the database.
                        LOGGER.log(Level.INFO, "Updating Clients status in the database"
                                + " on Status column edit commit handling...");
                        CLIENT_MANAGER.edit(newClient);
                        lblInputError.setVisible(false);
                    } catch(InternalServerErrorException ex) {
                        LOGGER.log(Level.SEVERE, ex.getMessage());
                        LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
                        showErroAlert(ex.getCause().getMessage());
                    }
                });
        } catch (InternalServerErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
            showErroAlert(ex.getCause().getMessage());
        }
    }

    /**
     * Chechs if the lenght of the given data is correct or not.
     *
     * @param data The given data.
     * @return True if the data is valid; False if not.
     */
    private boolean dataLengthIsValid(String data) {
        LOGGER.log(Level.INFO, "Starting data length validation...");
        boolean dataIsValid = true;

        if (data.length() == 0) {
            //Showing empty data error message...
            lblInputError.setText("* Fields must not be empty");
            lblInputError.setVisible(true);
            dataIsValid = false;
        } else if (data.length() > 255) {
            //Showing too long data error message...
            lblInputError.setText("* All data must be less than 255 characters.");
            lblInputError.setVisible(true);
            dataIsValid = false;
        } else {
            //Hiding error message label.
            lblInputError.setText("");
            lblInputError.setVisible(false);
        }

        return dataIsValid;
    }

    /**
     * Checks if the given email has a correct format or not.
     *
     * @param email The given email.
     * @return True if the format is valid; False if not.
     */
    private boolean emailPatternIsValid(String email) {
        LOGGER.log(Level.INFO, "Starting email pattern validation...");
        boolean emailIsValid = true;

        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = patternEmail.matcher(email);
        if (!matcher.matches()) { //If email does not match the pattern.
            //Showing email wrong format error messafe...
            lblInputError.setText("* Email must match the following format:\n   jsmith@example.com");
            lblInputError.setVisible(true);
            emailIsValid = false;
        } else {
            //Hiding error message label.
            lblInputError.setVisible(false);
        }

        return emailIsValid;
    }

    /**
     * Handles the table selection change event.
     *
     * @param observable Table selected item property.
     * @param oldVaue Old value of the observable property.
     * @param newValue New value of the observable property.
     */
    private void handleClientsTableSelectionChange(ObservableValue observable,
            Object oldVaue, Object newValue) {
        LOGGER.log(Level.INFO, "Handling table selection change...");
        if (newValue != null) { //A row of the table is selected.
            //Enable See Events button and menu item.
            btnSeeEvents.setDisable(false);
            menuItemSeeEvents.setDisable(false);
        } else { //There isn't any row selected.
            //Disable See Events button and menu item.
            btnSeeEvents.setDisable(true);
            menuItemSeeEvents.setDisable(true);
        }
    }
    
    /**
     * Shows an error Alert window with the specified message.
     * 
     * @param errorMessage The specified message.
     */
    private void showErroAlert(String errorMessage) {
        LOGGER.log(Level.INFO, "Showing Alert window with error message...");
        Alert errorAlert = new Alert(AlertType.ERROR, errorMessage, ButtonType.OK);
        errorAlert.show();
    }

    /**
     * Handles the creation of a new Client both in the table and database.
     *
     * @param event Event that is being handled.
     */
    @FXML
    private void handleNewClient(ActionEvent event) {
        LOGGER.log(Level.INFO, "Handling New Client creation request...");
        try {
            //Creating a Client with null data.
            Client newClient = new Client();
            //Setting default password to the new Client.
            newClient.setPassword(PublicCrypt.encode("abcd*1234"));
            //Setting newClient privilege to CLIENT.
            newClient.setUserPrivilege(UserPrivilege.CLIENT);
            //Setting User status to DISABLED.
            newClient.setUserStatus(UserStatus.DISABLED);
            //Setting random profile image to the new Client.
            Random random = new Random();
            newClient.setProfileImage(PROFILE_IMAGES[random.nextInt(PROFILE_IMAGES.length)]);
            //Register the new Client in the database.
            LOGGER.log(Level.INFO, "Updating database with new Client on New"
                    + " Client creation request handling...");
            CLIENT_MANAGER.create(newClient);
            clientsData = FXCollections.observableList(CLIENT_MANAGER
                    .getAllClients(new GenericType<List<Client>>() {
            }));
            //Removing the new Client so it does not appear at the end of the table.
            Client client = (Client) clientsData.remove(clientsData.size() - 1);
            //Adding the clientsData to the tabla view.
            tableClients.setItems(clientsData);
            //Adding the new Client to the beginning of the table.
            tableClients.getItems().add(0, client);
            //Refreshing the table so that the new Client appears.
            tableClients.refresh();
            //Select the new row and scroll to it.
            tableClients.getSelectionModel().select(0, tableColLogin);
        } catch (InternalServerErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
            showErroAlert(ex.getCause().getMessage());
        } catch (UnexpectedErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            showErroAlert(ex.getMessage());
        }

    }

    /**
     * Switches to Event Management window when See Events button or menu item
     * are pressed.
     *
     * @param event Event that is being handled.
     */
    @FXML
    private void handleSeeEvents(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "Redirecting to EventManagement window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/EventManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            EventManagementController controller = (loader.getController());
            //Setting stage on the controller.
            controller.setStage(stage);
            //Getting selected Client.
            Client selectedClient = (Client) tableClients
                    .getSelectionModel().getSelectedItem();
            //Setting selected Client on the controller.
            controller.setUserLogin(selectedClient.getLogin());
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to EventManagement window: {0}", ex.getMessage());
            showErroAlert("Could not switch to Event Management window due to an"
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
     *
     * @param stage The given stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}