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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Club;
import reto2desktopclient.security.PublicCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reto2desktopclient.exceptions.UnexpectedErrorException;

/**
 * Controller for the ClubProfile window.
 *
 * @author Ander
 */
public class ClubProfileController {

    /**
     * Logger used to leave traces.
     */
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
    private Button btnLogOut;

    boolean errorLoginLenght = false;
    boolean usernameExists = false;
    boolean errorNameLenght = false;
    boolean errorNamePattern = false;
    boolean errorLocationLenght = false;
    boolean errorPhoneNumberLenght = false;
    boolean errorPhoneNumberPattern = false;
    boolean errorPasswordLenght = false;
    boolean errorBiographyLenght = false;

    //Club that has logged in.
    private Club clubSign;

    //Gets the club that has logged in.
    public Club getClubSign() {
        return clubSign;
    }

    //Sets the club that has logged in.
    public void setClubSign(Club clubSign) {
        this.clubSign = clubSign;
    }

    /**
     * Initializes the stage of the window.
     *
     * @param root Parent of all nodes in the scene graph.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        Logger.getLogger(ClubManagementController.class.getName()).log(Level.INFO, "Initializing stage...");
        stage.setScene(scene);
        stage.setTitle("Club Profile");
        stage.setResizable(false);
        txtName.setText(clubSign.getFullName());
        txtLogin.setText(clubSign.getLogin());
        txtEmail.setText(clubSign.getEmail());
        txtLocation.setText(clubSign.getLocation());
        txtPhoneNumber.setText(clubSign.getPhoneNum());
        txtBiography.setText(clubSign.getBiography());
        stage.setOnShowing(this::handleWindowShowing);
        stage.hide();
        stage.show();
    }

    /**
     * Handles the showing event of the stage by initializing window elements.
     *
     * @param event Showing event of the stage.
     */
    private void handleWindowShowing(WindowEvent event) {
        lblErrorLogin.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorLocation.setVisible(false);
        lblErrorPhoneNumber.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorBiography.setVisible(false);
        btnSaveChanges.setDisable(true);
        txtEmail.setDisable(true);
        txtName.requestFocus();
        txtLogin.textProperty().addListener(this::handleTextLogin);
        txtName.textProperty().addListener(this::handleTextName);
        txtLocation.textProperty().addListener(this::handleTextLocation);
        txtPhoneNumber.textProperty().addListener(this::handleTextPhoneNumber);
        pwdPassword.textProperty().addListener(this::handleTextPassword);
        txtBiography.textProperty().addListener(this::handleTextBiography);

        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Showing stage...");
    }

    /**
     * Checks if login textfield is valid (lenght).
     *
     * @param obs
     */
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

    /**
     * Checks if name textfield is valid (lenght & pattern).
     *
     * @param obs
     */
    private void handleTextName(Observable obs) {
        lblErrorName.setText("");
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

    /**
     * Checks if location textfield is valid (lenght).
     *
     * @param obs
     */
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

    /**
     * Checks if phone number textfield is valid (lenght & pattern).
     *
     * @param obs
     */
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

    /**
     * Checks if password field is valid (lenght).
     *
     * @param obs
     */
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
            errorPasswordLenght = false;
            lblErrorPassword.setVisible(false);
        }
        testLabels();
    }

    /**
     * Checks if biography textarea is valid (lenght).
     *
     * @param obs
     */
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
            errorBiographyLenght = false;
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

    /**
     * After all validations updates club profile and shows an alert
     * confirmation.
     *
     * @param event
     * @throws UserInputException and ClientErrorException in case something
     * goes wrong.
     */
    @FXML
    public void handleButtonSaveChanges(ActionEvent event) throws UserInputException {
        Integer encontradoUsername = 0;
        usernameExists = false;
        try {
            Club club = new Club();
            List<Club> clubfind = ClubManagerFactory.getClubManager()
                    .getAllClubs(new GenericType<List<Club>>() {
                    });
            if (!clubSign.getLogin().equals(txtLogin.getText().toString())) {
                for (int x = 0; x < clubfind.size(); x++) {
                    if (clubfind.get(x).getLogin().equals(txtLogin.getText())) {
                        usernameExists = true;
                        encontradoUsername = x;
                        break;
                    } else {
                        usernameExists = false;
                    }
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
            clubSign.setPassword(encodedPassword);
            clubSign.setFullName(txtName.getText());
            clubSign.setLocation(txtLocation.getText());
            clubSign.setPhoneNum(txtPhoneNumber.getText());
            clubSign.setBiography(txtBiography.getText().toString());
            ClubManagerFactory.getClubManager().edit(clubSign);
            LOGGER.log(Level.INFO, "Club profile was updated succesfuly");
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION, "Profile was updated succesfuly!", ButtonType.OK);
            alert.showAndWait();
        } catch (ClientErrorException | UnexpectedErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * Switches to log in window.
     *
     * @param event
     * @throws ClientErrorException or IOException in case something goes wrong.
     */
    @FXML
    public void handleButtonLogOut(ActionEvent event) throws UserInputException, IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Logging out...");
            controller.initStage(root);
        } catch (ClientErrorException | IOException ex) {
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
