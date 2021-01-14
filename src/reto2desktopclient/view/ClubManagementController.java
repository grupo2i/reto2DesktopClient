/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.UserStatus;

/**
 *
 * @author Ander
 */
public class ClubManagementController {

    @FXML
    private Stage stage;
    @FXML
    private TableView clubTable;
    @FXML
    private TableColumn tableId;
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
    private Label lblId;
    @FXML
    private TextField txtId;
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
    private Button btnBack;
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

    boolean errorLoginLenght = false;
    boolean usernameExists = false;
    boolean errorEmailLenght = false;
    boolean errorEmailPattern = false;
    boolean emailExists = false;
    boolean errorNameLenght = false;
    boolean errorNamePattern = false;
    boolean errorLocationLenght = false;
    boolean errorPhoneNumberLenght = false;
    boolean errorPhoneNumberPattern = false;
    boolean errorStatus = false;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        Logger.getLogger(ClubManagementController.class.getName()).log(Level.INFO, "Initializing stage...");
        stage.setScene(scene);
        stage.setTitle("Club Management");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);

        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void handleWindowShowing(WindowEvent event) {
        lblErrorLogin.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorLocation.setVisible(false);
        lblErrorPhoneNumber.setVisible(false);
        lblErrorStatus.setVisible(true);
        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSeeEvents.setDisable(true);
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

        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Showing stage...");
    }

    private void handleTextLogin(Observable obs) {
        Integer usLenght = txtLogin.getText().trim().length();
        //if username =0 or <255= error
        if (usLenght == 0 || usLenght > 255) {
            errorLoginLenght = true;
            if (usLenght == 0) {
                lblErrorLogin.setText("* Field must not be empty");
            } else if (usLenght > 255) {
                lblErrorLogin.setText("* Must be less than 255 characters");
            }
            lblErrorLogin.setVisible(true);
        } else {
            errorLoginLenght = false;
            lblErrorLogin.setVisible(false);
        }
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
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtEmailLength > 255) {
                lblErrorEmail.setText("* Must be < than 255");
            } //Sets the error message when the field does not match the pattern.
            else if (!matcherEmail.matches()) {
                lblErrorEmail.setText("* Must match: a@a.aa");
            }
            lblErrorEmail.setVisible(true);
            errorEmailPattern = false;
            btnAdd.setDisable(true);
        } else {
            errorEmailLenght = false;
            lblErrorEmail.setVisible(false);
        }

    }

    private void handleTextName(Observable obs) {
        Integer txtNameLength = txtName.getText().trim().length();
        Pattern patternName = Pattern.compile("^([A-Za-z]+[ ]?)+$");
        Matcher matcherName = patternName.matcher(txtName.getText());

        if (txtNameLength == 0 || txtNameLength > 10 || !matcherName.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtNameLength == 0) lblErrorName.setText("* Field must not be empty");
            //Sets the error message when the field is longer than 255 characters.
            else if (txtNameLength > 10) lblErrorName.setText("* Must be less than 255 characters");
            //Sets the error message when the field does not match the pattern.
            else if (!matcherName.matches()) lblErrorName.setText("* Must only contain letters");
            
            btnAdd.setDisable(true);
            lblErrorName.setVisible(true);
        } else {
            lblErrorName.setVisible(false);
        }

    }

    private void handleTextLocation(Observable obs) {
        Integer txtLocationLength = txtName.getText().trim().length();
        if (txtLocationLength == 0 || txtLocationLength > 255) {
            errorLocationLenght = true;
            btnAdd.setDisable(false);
        } else {
            errorLocationLenght = false;
            btnAdd.setDisable(true);
        }
    }

    private void handleTextPhoneNumber(Observable obs) {
        Integer txtPhoneNumberLenght = txtName.getText().trim().length();
        Pattern patternPhoneNumber = Pattern.compile("/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/");
        Matcher matcherPhoneNumber = patternPhoneNumber.matcher(txtPhoneNumber.getText());
        if (txtPhoneNumberLenght == 0 || txtPhoneNumberLenght > 255) {
            errorPhoneNumberLenght = true;
        } else if (!matcherPhoneNumber.matches()) {
            errorPhoneNumberPattern = true;
        } else {
            errorPhoneNumberLenght = false;
            errorPhoneNumberPattern = false;
        }
    }

    private void handleTextStatus(Observable obs) {
        String status = txtStatus.getText();
        if (status.equals("Enabled") || status.equals("enabled") || status.equals("Disabled")
                || status.equals("disabled")) {
            errorStatus = false;
        } else {
            errorStatus = true;
        }
    }

    @FXML
    public void handleButtonAdd(ActionEvent event) throws UserInputException {
        try {
            Club club = new Club();
            ArrayList<Club> clubfind = new ArrayList<>();
            clubfind = ClubManagerFactory.getClubManager().getAllClubs(ArrayList.class);
            for (Integer x = 0; x < clubfind.size(); x++) {
                if (clubfind.get(x).getLogin().equals(txtLogin.getText())) {
                    usernameExists = true;
                } else {
                    usernameExists = false;
                }
            }
            if (usernameExists) {
                throw new UserInputException("Username already exists");
            } else {
                club.setLogin(txtLogin.getText());
            }
            ArrayList<Club> clubmail = new ArrayList<>();
            clubmail = ClubManagerFactory.getClubManager().getAllClubs(ArrayList.class);
            for (Integer x = 0; x < clubmail.size(); x++) {
                if (clubmail.get(x).getEmail().equals(txtEmail.getText())) {
                    emailExists = true;
                } else {
                    emailExists = false;
                }
            }
            if (emailExists) {
                throw new UserInputException("Email already exists");
            } else {
                club.setEmail(txtEmail.getText());
            }
            club.setFullName(txtName.getText());
            club.setLocation(txtLocation.getText());
            club.setPhoneNum(txtPhoneNumber.getText());
            club.setUserStatus(UserStatus.valueOf(txtStatus.getText()));
            ClubManagerFactory.getClubManager().create(club);
        } catch (ClientErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

}
