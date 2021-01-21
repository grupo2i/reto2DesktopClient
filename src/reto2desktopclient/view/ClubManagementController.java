/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClientManagerFactory;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UnexpectedErrorException;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Client;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.UserPrivilege;
import reto2desktopclient.model.UserStatus;
import reto2desktopclient.security.PublicCrypt;

/**
 *
 * @author Ander
 */
public class ClubManagementController {

    private static final Logger LOGGER = Logger.getLogger(ClubManagementController.class.getName());

    @FXML
    private Stage stage;
    @FXML
    private TableView clubTable;
    @FXML
    private TableColumn tableLogin;
    @FXML
    private TableColumn tableEmail;
    @FXML
    private TableColumn tableName;
    @FXML
    private TableColumn tableLastAccess;
    @FXML
    private TableColumn tableLocation;
    @FXML
    private TableColumn tablePhoneNumber;
    @FXML
    private TableColumn tableStatus;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblLogin;
    @FXML
    private TextField txtLogin;
    @FXML
    private Label lblEmail;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblLocation;
    @FXML
    private TextField txtLocation;
    @FXML
    private Label lblPhoneNumber;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSeeEvents;
    @FXML
    private Label lblErrorLogin;
    @FXML
    private Label lblErrorEmail;
    @FXML
    private Label lblErrorName;
    @FXML
    private Label lblErrorLocation;
    @FXML
    private Label lblErrorPhoneNumber;
    @FXML
    private Label lblErrorStatus;
    @FXML
    private AdminMenuController adminMenuController;

    private ObservableList clubData;

    boolean errorLoginLenght = true;
    boolean usernameExists = true;
    boolean errorEmailLenght = true;
    boolean errorEmailPattern = true;
    boolean emailExists = true;
    boolean errorNameLenght = true;
    boolean errorNamePattern = true;
    boolean errorLocationLenght = true;
    boolean errorPhoneNumberLenght = true;
    boolean errorPhoneNumberPattern = true;
    boolean errorStatusLenght = true;
    boolean errorStatusPattern = true;
    boolean tableIsSelected = false;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        Logger.getLogger(ClubManagementController.class.getName()).log(Level.INFO, "Initializing stage...");
        stage.setScene(scene);
        stage.setTitle("Club Management");
        stage.setResizable(false);
        adminMenuController.setStage(stage);
        stage.setOnShowing(this::handleWindowShowing);
        Logger.getLogger(ClubManagementController.class.getName()).log(Level.INFO, "Showing stage...");
        stage.hide();
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void handleWindowShowing(WindowEvent event) {
        try {
            lblErrorLogin.setVisible(false);
            lblErrorEmail.setVisible(false);
            lblErrorName.setVisible(false);
            lblErrorLocation.setVisible(false);
            lblErrorPhoneNumber.setVisible(false);
            lblErrorStatus.setVisible(false);
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            btnSeeEvents.setDisable(true);
            txtLogin.requestFocus();
            btnAdd.setTooltip(
                    new Tooltip("Insert data to add club"));
            btnDelete.setTooltip(
                    new Tooltip("Select club to disable"));
            btnUpdate.setTooltip(
                    new Tooltip("Select club to update"));
            txtLogin.textProperty().addListener(this::handleTextLogin);
            txtEmail.textProperty().addListener(this::handleTextEmail);
            txtName.textProperty().addListener(this::handleTextName);
            txtLocation.textProperty().addListener(this::handleTextLocation);
            txtPhoneNumber.textProperty().addListener(this::handleTextPhoneNumber);
            txtStatus.textProperty().addListener(this::handleTextStatus);

            //Stablishing cell value factories on table columns...
            tableLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
            tableEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            tableName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            tableLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
            tableLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            tablePhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
            tableStatus.setCellValueFactory(new PropertyValueFactory<>("userStatus"));
            //Creating a Club observable List with all registered clubs.
            clubData = FXCollections.observableList(ClubManagerFactory
                    .getClubManager().getAllClubs(new GenericType<List<Club>>() {
                    }));
            //Setting the table model to the observable list above.
            clubTable.setItems(clubData);
            //Setting table selection listener.
            clubTable.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleClubTableSelectionChange);

            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Showing stage...");
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }

    }

    private void handleClubTableSelectionChange(ObservableValue observable,
            Object oldVaue, Object newValue) {
        tableIsSelected=true;
        if (newValue != null) { //A row of the table is selected.
            //Enable See Events, delete and update buttons.
            btnSeeEvents.setDisable(false);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            Club selectedClub = ((Club) clubTable.getSelectionModel().getSelectedItem());
            txtEmail.setText(selectedClub.getEmail());
            txtLocation.setText(selectedClub.getLocation());
            txtLogin.setText(selectedClub.getLogin());
            txtName.setText(selectedClub.getFullName());
            txtPhoneNumber.setText(selectedClub.getPhoneNum());
            txtStatus.setText(selectedClub.getUserStatus().toString());
            btnAdd.setDisable(true);
            tableIsSelected=true;
        } else { //There isn't any row selected.
            //Disable all buttons.
            btnSeeEvents.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            btnAdd.setDisable(true);
            resetFieldsAndLabels(); 
            tableIsSelected=false;
        }
    }

    private void handleTextLogin(Observable obs) {
        Integer usLenght = txtLogin.getText().trim().length();
        //if username =0 or <255= error
        if (usLenght == 0 || usLenght > 255) {
            errorLoginLenght = true;
            if (usLenght == 0) {
                lblErrorLogin.setText("* Field must not be empty");
            } else if (usLenght > 255) {
                lblErrorLogin.setText("* Must be < than 255");
            }
            lblErrorLogin.setVisible(true);
            errorLoginLenght = true;
        } else {
            errorLoginLenght = false;
            lblErrorLogin.setVisible(false);
        }
        testLabels();
    }

    private void handleTextEmail(Observable obs) {
        Integer txtEmailLength = txtEmail.getText().trim().length();
        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcherEmail = patternEmail.matcher(txtEmail.getText());

        if (txtEmailLength == 0 || txtEmailLength > 255 || !matcherEmail.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtEmailLength == 0) {
                lblErrorEmail.setText("* Field must not be empty");
                errorEmailLenght = false;
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtEmailLength > 255) {
                lblErrorEmail.setText("* Must be < than 255");
                errorEmailLenght = false;
            } //Sets the error message when the field does not match the pattern.
            else if (!matcherEmail.matches()) {
                lblErrorEmail.setText("* Must match: a@a.aa");
                errorEmailPattern = true;
            }
            lblErrorEmail.setVisible(true);
            errorEmailPattern = true;
        } else {
            errorEmailLenght = false;
            lblErrorEmail.setVisible(false);
            errorEmailPattern = false;
        }
        testLabels();
    }

    private void handleTextName(Observable obs) {
        Integer txtNameLength = txtName.getText().trim().length();
        Pattern patternName = Pattern.compile("^([A-zÀ-ú]+[ ]?)+$");
        Matcher matcherName = patternName.matcher(txtName.getText());

        if (txtNameLength == 0 || txtNameLength > 255 || !matcherName.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtNameLength == 0) {
                lblErrorName.setText("* Field must not be empty");
                errorNameLenght = true;
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtNameLength > 255) {
                lblErrorName.setText("* Must be < than 255");
                errorNameLenght = true;
            } //Sets the error message when the field does not match the pattern.
            else if (!matcherName.matches()) {
                lblErrorName.setText("* Must only contain letters");
                errorNamePattern = true;
            }
            lblErrorName.setVisible(true);
        } else {
            lblErrorName.setVisible(false);
            errorNameLenght = false;
            errorNamePattern = false;
        }
        testLabels();
    }

    private void handleTextLocation(Observable obs) {
        Integer txtLocationLength = txtLocation.getText().trim().length();
        if (txtLocationLength == 0 || txtLocationLength > 255) {
            //Sets the error message when the fiel is empty.
            if (txtLocationLength == 0) {
                lblErrorLocation.setText("* Field must not be empty");
                errorLocationLenght = true;
            } //Sets the error message when the field is longer than 255 characters.
            if (txtLocationLength > 255) {
                lblErrorLocation.setText("* Must be < than 255");
                errorLocationLenght = true;
            }
            lblErrorLocation.setVisible(true);
            btnAdd.setDisable(true);
        } else {
            errorLocationLenght = false;
            lblErrorLocation.setVisible(false);
        }
        testLabels();
    }

    private void handleTextPhoneNumber(Observable obs) {
        Integer txtPhoneNumberLenght = txtPhoneNumber.getText().trim().length();
        Pattern patternPhoneNumber = Pattern.compile("[0-9]+");
        Matcher matcherPhoneNumber = patternPhoneNumber.matcher(txtPhoneNumber.getText());
        if (txtPhoneNumberLenght == 0 || txtPhoneNumberLenght > 255 || txtPhoneNumber.getText().matches("[a-zA-Z]+")) {
            //Sets the error message when the fiel is empty.
            if (txtPhoneNumberLenght == 0) {
                lblErrorPhoneNumber.setText("* Field must not be empty");
                errorPhoneNumberLenght = true;
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtPhoneNumberLenght > 255) {
                lblErrorPhoneNumber.setText("* Must be < than 255");
                errorPhoneNumberLenght = true;
            } else if (txtPhoneNumber.getText().matches("[a-zA-Z]+")) {
                lblErrorPhoneNumber.setText("* Only numbers allowed");
                errorPhoneNumberPattern = true;
            }
            lblErrorPhoneNumber.setVisible(true);
        } else {
            lblErrorPhoneNumber.setVisible(false);
            errorPhoneNumberLenght = false;
            errorPhoneNumberPattern = false;
        }
        testLabels();
    }

    private void handleTextStatus(Observable obs) {
        String status = txtStatus.getText();
        Integer txtStatusLenght = txtStatus.getText().trim().length();

        if (txtStatusLenght == 0 || txtStatusLenght > 255) {
            //Sets the error message when the fiel is empty.
            if (txtStatusLenght == 0) {
                lblErrorStatus.setText("* Field must not be empty");
                errorStatusLenght = true;
            } //Sets the error message when the field is longer than 255 characters.
            if (txtStatusLenght > 255) {
                lblErrorStatus.setText("* Must be < than 255");
                errorStatusLenght = true;
            }
            lblErrorStatus.setVisible(true);
            btnAdd.setDisable(true);
        } else {
            if (!status.equalsIgnoreCase("enabled") && !status.equalsIgnoreCase("disabled")) {
                lblErrorStatus.setText("* Enter Enabled or Disabled");
                lblErrorStatus.setVisible(true);
                btnAdd.setDisable(true);
                errorStatusPattern = true;
            } else {
                errorStatusLenght = false;
                errorStatusPattern = false;
                lblErrorStatus.setVisible(false);
            }
        }
        testLabels();
    }

    @FXML
    public void handleButtonAdd(ActionEvent event) throws UserInputException {
        Integer encontradoUsername = 0;
        Integer encontradoMail = 0;

        try {
            usernameExists = findLogin();
            Club club = new Club();
            if (usernameExists) {
                LOGGER.log(Level.SEVERE, "Login username already in use");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Login username already exists", ButtonType.OK);
                alert.showAndWait();
                btnAdd.setDisable(true);
                btnUpdate.setDisable(true);
            } else {
                club.setLogin(txtLogin.getText());
                btnAdd.setDisable(false);
                btnUpdate.setDisable(false);
            }
            emailExists = findEmail();
            if (emailExists) {
                LOGGER.log(Level.SEVERE, "Email already in use");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Email already exists", ButtonType.OK);
                alert.showAndWait();
                btnAdd.setDisable(true);
                btnUpdate.setDisable(true);
            } else {
                club.setEmail(txtEmail.getText());
                btnAdd.setDisable(false);
                btnUpdate.setDisable(false);
            }
            String password = "defaultPassword";
            String encodedPassword = PublicCrypt.encode(password);
            club.setPassword(encodedPassword);
            club.setUserPrivilege(UserPrivilege.CLUB);
            club.setFullName(txtName.getText());
            club.setLocation(txtLocation.getText());
            club.setPhoneNum(txtPhoneNumber.getText());
            club.setUserStatus(UserStatus.valueOf(txtStatus.getText().toUpperCase()));
            club.setLastPasswordChange(Timestamp.valueOf(LocalDateTime.now()));
            club.setProfileImage("user.png");
            ClubManagerFactory.getClubManager().create(club);
            clubData = FXCollections.observableList(ClubManagerFactory
                    .getClubManager().getAllClubs(new GenericType<List<Club>>() {
                    }));
            clubTable.setItems(clubData);
            LOGGER.log(Level.INFO, "Club was added succesfuly");
            resetFieldsAndLabels();
          
        } catch (ClientErrorException | UnexpectedErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    @FXML
    public void handleButtonDelete(ActionEvent event) throws UserInputException {
        try {
            //Get selected club data from table view model
            Club selectedClub = ((Club) clubTable.getSelectionModel().getSelectedItem());
            //Ask user for confirmation on delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected club?\n"
                    + "This option can't be reversed.",
                    ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            //If OK to deletion
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //delete user from server side
                ClubManagerFactory.getClubManager().remove(selectedClub.getId().toString());
                //clears editing fields
                resetFieldsAndLabels();
                //Clear selection and refresh table view 
                clubTable.getSelectionModel().clearSelection();
                clubData = FXCollections.observableList(ClubManagerFactory
                        .getClubManager().getAllClubs(new GenericType<List<Club>>() {
                        }));
                clubTable.setItems(clubData);
                LOGGER.log(Level.INFO, "Club was deleted succesfuly");
            }
        } catch (ClientErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    @FXML
    public void handleButtonUpdate(ActionEvent event) throws UserInputException {
        Club selectedClub = ((Club) clubTable.getSelectionModel().getSelectedItem());
        handleTextLogin(clubData);
        handleTextEmail(clubData);
        handleTextLocation(clubData);
        handleTextName(clubData);
        handleTextPhoneNumber(clubData);
        handleTextStatus(clubData);
        usernameExists = false;
        emailExists = false;
        if (!selectedClub.getLogin().equals(txtLogin.getText())) {
            usernameExists = findLogin();
        }
        if (usernameExists) {
            LOGGER.log(Level.SEVERE, "Login username already in use");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Login username already exists", ButtonType.OK);
            alert.showAndWait();
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
        } else {
            selectedClub.setLogin(txtLogin.getText());
            btnAdd.setDisable(false);
            btnUpdate.setDisable(false);
        }
        if (!selectedClub.getEmail().equals(txtEmail.getText())) {
            emailExists = findEmail();
        }
        if (emailExists) {
            LOGGER.log(Level.SEVERE, "Email already in use");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Email already in use", ButtonType.OK);
            alert.showAndWait();
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
        } else {
            selectedClub.setEmail(txtEmail.getText());
            btnAdd.setDisable(false);
            btnUpdate.setDisable(false);
        }
        selectedClub.setLocation(txtLocation.getText());
        selectedClub.setFullName(txtName.getText());
        selectedClub.setPhoneNum(txtPhoneNumber.getText());
        selectedClub.setUserStatus(UserStatus.valueOf(txtStatus.getText().toUpperCase()));
        ClubManagerFactory.getClubManager().edit(selectedClub);
    }

    @FXML
    public void handleButtonSeeEvents(ActionEvent event) throws UserInputException {
        Club selectedClub = ((Club) clubTable.getSelectionModel().getSelectedItem());
        try {
            LOGGER.log(Level.INFO, "Redirecting to event window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/EventManagement.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            EventManagementController controller = (loader.getController());
            //Setting club login to display its events
            controller.setUserLogin(selectedClub.getLogin());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to EventManagement window: {0}", ex.getMessage());
        }
    }

    private void testLabels() {
        if (errorStatusPattern || errorStatusPattern || errorNameLenght || errorNamePattern || errorEmailPattern
                || errorEmailLenght || errorLocationLenght || errorPhoneNumberLenght || errorPhoneNumberPattern) {
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
        } else {
            btnAdd.setDisable(false); 
            if(tableIsSelected)
            btnUpdate.setDisable(false);
            else
            btnUpdate.setDisable(true);    
        }
    }

    private void resetFieldsAndLabels() {
        txtEmail.setText("");
        txtLocation.setText("");
        txtLogin.setText("");
        txtName.setText("");
        txtPhoneNumber.setText("");
        txtStatus.setText("");
        lblErrorEmail.setVisible(false);
        lblErrorLocation.setVisible(false);
        lblErrorLogin.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorPhoneNumber.setVisible(false);
        lblErrorStatus.setVisible(false);
    }

    private boolean findLogin() {
        List<Club> clubfind = ClubManagerFactory.getClubManager()
                .getAllClubs(new GenericType<List<Club>>() {
                });

        for (int x = 0; x < clubfind.size(); x++) {
            if (clubfind.get(x).getLogin().equals(txtLogin.getText())) {
                usernameExists = true;
                break;
            } else {
                usernameExists = false;
            }
        }
        return usernameExists;
    }

    private boolean findEmail() {
        List<Club> clubfindmail = ClubManagerFactory.getClubManager()
                .getAllClubs(new GenericType<List<Club>>() {
                });
        for (int x = 0; x < clubfindmail.size(); x++) {
            if (clubfindmail.get(x).getEmail().equals(txtEmail.getText())) {
                emailExists = true;
                break;
            } else {
                emailExists = false;
            }
        }
        return emailExists;
    }
}
