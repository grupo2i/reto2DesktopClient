/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.UserPrivilege;
import reto2desktopclient.model.UserStatus;
import reto2desktopclient.security.PublicCrypt;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ander
 */
public class ClubProfileController {
    private static final Logger LOGGER = Logger.getLogger(ClubProfileController.class.getName());

    @FXML
    private Stage stage;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblErrorName;
    @FXML
    private Label lblLogin;
    @FXML
    private TextField txtLogin;
    @FXML
    private Label lblErrorLogin;
    @FXML
    private Label lblEmail;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblErrorEmail;
    @FXML
    private Label lblLocation;
    @FXML
    private TextField txtLocation;
    @FXML
    private Label lblErrorLocation;
    @FXML
    private Label lblPhoneNumber;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private Label lblErrorPhoneNumber;
    @FXML
    private Label lblPassword;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private Label lblErrorPassword;
    @FXML
    private Label lblBiography;
    @FXML
    private TextArea txtBiography;
    @FXML
    private Label lblErrorBiography;
    @FXML
    private Button btnSaveChanges;
    @FXML
    private ComboBox comboImage;
    @FXML
    private ImageView clubImage;

    boolean errorLoginLenght = true;
    boolean usernameExists = true;
    boolean errorNameLenght = true;
    boolean errorNamePattern = true;
    boolean errorLocationLenght = true;
    boolean errorPhoneNumberLenght = true;
    boolean errorPhoneNumberPattern = true;
    boolean errorPasswordLenght = true;
    boolean errorBiographyLenght = true;
    private Club clubSign;

    public Club getClubSign() {
        return clubSign;
    }

    public void setClubSign(Club clubSign) {
        this.clubSign = clubSign;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        Logger.getLogger(ClubManagementController.class.getName()).log(Level.INFO, "Initializing stage...");
        stage.setScene(scene);
        stage.setTitle("Club Profile");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
       /* txtName.setText(clubSign.getFullName());
        txtLogin.setText(clubSign.getLogin());
        txtEmail.setText(clubSign.getEmail());
        txtLocation.setText(clubSign.getLocation());
        txtPhoneNumber.setText(clubSign.getPhoneNum());
        pwdPassword.setText(clubSign.getPassword());
        txtBiography.setText(clubSign.getBiography());*/
        stage.show();
    }

    private void handleWindowShowing(WindowEvent event) {
        lblErrorLogin.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorLocation.setVisible(false);
        lblErrorPhoneNumber.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorBiography.setVisible(false);
        btnSaveChanges.setDisable(true);
        txtEmail.setText("Aqui va el mail del club");
        txtName.requestFocus();
        txtLogin.textProperty().addListener(this::handleTextLogin);
        txtName.textProperty().addListener(this::handleTextName);
        txtLocation.textProperty().addListener(this::handleTextLocation);
        txtPhoneNumber.textProperty().addListener(this::handleTextPhoneNumber);
        pwdPassword.textProperty().addListener(this::handleTextPassword);
        txtBiography.textProperty().addListener(this::handleTextBiography);

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
            btnSaveChanges.setDisable(true);
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

    private void handleTextPassword(Observable obs) {
        Integer pwdPasswordLength = pwdPassword.getText().trim().length();

        //If there is any error with pwdPassword...
        if (pwdPasswordLength < 6 || pwdPasswordLength > 255) {
            //Sets the error message when the fiel is shorter than 6 characters.
            if (pwdPasswordLength < 6) {
                lblErrorPassword.setText("* Must be at least 6 characters");
            } //Sets the error message when the field is longer than 255 characters.
            else if (pwdPasswordLength > 255) {
                lblErrorPassword.setText("* Must be less than 255 characters");
            }
            errorPasswordLenght = true;
            lblErrorPassword.setVisible(true);
        } else {
            errorPasswordLenght = true;
            lblErrorPassword.setVisible(false);
        }
        testLabels();
    }

    private void handleTextBiography(Observable obs) {
        Integer txtBiographyLength = txtBiography.getText().trim().length();

        //If there is any error with pwdPassword...
        if (txtBiographyLength < 6 || txtBiographyLength > 255) {
            //Sets the error message when the fiel is shorter than 6 characters.
            if (txtBiographyLength < 6) {
                lblErrorBiography.setText("* Must be at least 6 characters");
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtBiographyLength > 255) {
                lblErrorBiography.setText("* Must be less than 255 characters");
            }
            errorBiographyLenght = true;
            lblErrorBiography.setVisible(true);
        } else {
            errorBiographyLenght = true;
            lblErrorBiography.setVisible(false);
        }
        testLabels();
    }

    private void testLabels() {

        if (errorPasswordLenght || errorBiographyLenght || errorNameLenght
                || errorNamePattern || errorLocationLenght || errorPhoneNumberLenght
                || errorPhoneNumberPattern || errorLoginLenght) {
            btnSaveChanges.setDisable(true);
        } else {
            btnSaveChanges.setDisable(false);
        }
    }

    @FXML
    public void handleButtonSaveChanges(ActionEvent event) throws UserInputException {
        Integer encontradoUsername = 0;

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
            String encodedPassword = PublicCrypt.encode(pwdPassword.getText());
            club.setPassword(encodedPassword);
            club.setFullName(txtName.getText());
            club.setLocation(txtLocation.getText());
            club.setPhoneNum(txtPhoneNumber.getText());
            club.setBiography(txtBiography.getText().toString());
            ClubManagerFactory.getClubManager().edit(club);
            LOGGER.log(Level.INFO, "Club profile was updated succesfuly");
        } catch (ClientErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
