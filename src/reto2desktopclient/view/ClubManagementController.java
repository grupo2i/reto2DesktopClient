/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.UserPrivilege;
import reto2desktopclient.model.UserStatus;

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
        Pattern patternName = Pattern.compile("^([A-Za-z]+[ ]?)+$");
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
            Club club = new Club();
            List<Club> clubfind = ClubManagerFactory.getClubManager()
                    .getAllClubs(new GenericType<List<Club>>() {
                    });

            for (int x = 0; x < clubfind.size(); x++) {
                if (clubfind.get(x).getLogin().equals(txtLogin.getText())) {
                    usernameExists = true;
                    encontradoUsername = x;
                    break;
                } else {
                    usernameExists = false;
                }
            }
            if (usernameExists) {
                LOGGER.log(Level.SEVERE, "Username with login: " + clubfind.get(encontradoUsername).getLogin() + " already in use");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Username " + clubfind.get(encontradoUsername).getLogin() + " already exists", ButtonType.OK);
                alert.showAndWait();
            } else {
                club.setLogin(txtLogin.getText());
            }
            Club clubmail = new Club();
            List<Club> clubfindmail = ClubManagerFactory.getClubManager()
                    .getAllClubs(new GenericType<List<Club>>() {
                    });
            for (int x = 0; x < clubfindmail.size(); x++) {
                if (clubfindmail.get(x).getEmail().equals(txtEmail.getText())) {
                    emailExists = true;
                    encontradoMail = x;
                    break;
                } else {
                    emailExists = false;
                }
            }
            if (emailExists) {
                LOGGER.log(Level.SEVERE, "Email: " + clubfindmail.get(encontradoMail).getEmail() + " already in use");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Email " + clubfind.get(encontradoMail).getEmail() + " already exists", ButtonType.OK);
                alert.showAndWait();
            } else {
                club.setEmail(txtEmail.getText());
            }
            String password="defaultPassword";
            club.setPassword(password);
            club.setUserPrivilege(UserPrivilege.CLUB);
            club.setBiography("HOLA HOLA");
            club.setFullName(txtName.getText());
            club.setLocation(txtLocation.getText());
            club.setPhoneNum(txtPhoneNumber.getText());
            club.setUserStatus(UserStatus.valueOf(txtStatus.getText().toUpperCase()));
            ClubManagerFactory.getClubManager().create(club);
            LOGGER.log(Level.INFO, "Club was added succesfuly");
        } catch (ClientErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    private void testLabels() {

        if (errorStatusPattern || errorStatusPattern || errorNameLenght || errorNamePattern || errorEmailPattern
                || errorEmailLenght || errorLocationLenght || errorPhoneNumberLenght || errorPhoneNumberPattern) {
            btnAdd.setDisable(true);
        } else {
            btnAdd.setDisable(false);
        }
    }
}
