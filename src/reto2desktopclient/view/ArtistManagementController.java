package reto2desktopclient.view;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reto2desktopclient.model.Artist;

/**
 *
 * @author Matteo Fernández
 */
public class ArtistManagementController {

    private static final Logger LOGGER = Logger.getLogger(LogInController.class.getName());
    private Stage stage;
    @FXML
    private Button btnUpdateArtist;
    @FXML
    private Button btnDeleteArtist;
    @FXML
    private Button btnAddArtist;
    @FXML
    private TextField txtUserNameArtist;
    @FXML
    private TextField txtEmailArtist;
    @FXML
    private RadioButton btnE;
    @FXML
    private RadioButton btnD;
    @FXML
    private TextField txtFullNameArtist;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Label lblNameError1;
    @FXML
    private Label lblEmailError1;
    @FXML
    private Label lblUsernameError1;
    @FXML
    private TableView<Artist> tbData;
    @FXML
    public TableColumn<Artist, String> tblLogin;
    @FXML
    public TableColumn<Artist, String> tbEmail;

    @FXML
    public TableColumn<Artist, String> tblName;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TableColumn<Artist, String> musicGenre;

    @FXML
    public TableColumn<Artist, String> tblStatus;
    @FXML
    public TableColumn<Artist, LocalDate> tblLastaccess;
    ToggleGroup group = new ToggleGroup();

    boolean errorEmailLenght = true;
    boolean errorEmailPattern = true;
    boolean errorTxtUserNameArtist = true;
    boolean errorTxtFullNameArtist = true;

    public void initStage(Parent root
    ) {
        //Initialize the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Artist Management");
        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Initializing stage...");

        txtEmailArtist.textProperty().addListener(this::handletxtEmailArtist);
        txtFullNameArtist.textProperty().addListener(this::handleTextFullNameArtist);
        txtUserNameArtist.textProperty().addListener(this::handletxtUserNameArtist);

        btnAddArtist.setDisable(true);
        btnDeleteArtist.setDisable(true);
        btnUpdateArtist.setDisable(true);

        initializebottonGroup();
        lblNameError1.setVisible(false);
        lblEmailError1.setVisible(false);
        lblUsernameError1.setVisible(false);
        //Calls the ChoiceBox function
        initializeCheckBox();
        initialize();
        datePicker.setValue(LocalDate.now());
        //Shows the stage
        stage.show();
        LOGGER.log(Level.INFO, "Successfully switched to Artist window.");
    }
    /**
     * Check that the full name pattern is correct
     *
     * @param obs
     */
    public void handleTextFullNameArtist(Observable obs) {
        Integer txtFullNameLength = txtFullNameArtist.getText().trim().length();
        Pattern patternFullName = Pattern.compile("^([A-Za-z]+[ ]?)+$");
        Matcher matcherFullName = patternFullName.matcher(txtFullNameArtist.getText());
        //If there is any error...
        if (txtFullNameLength == 0 || txtFullNameLength > 255 || !matcherFullName.matches()) {
            if (!matcherFullName.matches()) {
                disableButtons();
                lblNameError1.setVisible(true);
            }
            //Sets the alert message when the fiel is empty.
            if (txtFullNameLength == 0) {
                disableButtons();
                lblNameError1.setVisible(true);
            } //Sets the alert message when the field is longer than 255 characters.
            else if (txtFullNameLength > 255) {
                disableButtons();
                lblNameError1.setVisible(true);
            }
        } else {
            enableButtons();
            lblNameError1.setVisible(false);

        }
    }

    /**
     * Check that the Username pattern is correct
     *
     * @param obs
     */
    public void handletxtUserNameArtist(Observable obs) {
        Integer usLenght = txtUserNameArtist.getText().trim().length();
        //if username =0 or <255= error
        if (usLenght == 0 || usLenght > 255) {
            disableButtons();
            lblUsernameError1.setVisible(true);
        } else {
            enableButtons();
            lblUsernameError1.setVisible(false);
        }
    }

    /**
     * Creates the values for the choice box
     */
    @FXML
    public void initializeCheckBox() {
        choiceBox.setItems(FXCollections.observableArrayList(
                "POP", "ROCK", "REGGAE", "EDM", "TRAP", "RAP", "INDIE", "REGGAETON", new Separator(), "OTHER"));
        choiceBox.setTooltip(new Tooltip("Select the music genre"));
        choiceBox.setValue("POP");
    }

    @FXML
    public void initializebottonGroup() {
        btnD.setToggleGroup(group);
        btnE.setToggleGroup(group);
        btnE.setSelected(true);
    }

    /**
     * Check that the email pattern is correct
     *
     * @param obs
     */
    public void handletxtEmailArtist(Observable obs) {
        Integer txtEmailLength = txtEmailArtist.getText().trim().length();
        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcherEmail = patternEmail.matcher(txtEmailArtist.getText());

        if (txtEmailLength == 0 || txtEmailLength > 255) {
            disableButtons();
            lblEmailError1.setVisible(true);
        } else if (!matcherEmail.matches()) {
            disableButtons();
            lblEmailError1.setVisible(true);
        } else {
            enableButtons();
            lblEmailError1.setVisible(false);
        }

    }

    public void enableButtons() {
        btnAddArtist.setDisable(false);
        btnDeleteArtist.setDisable(false);
        btnUpdateArtist.setDisable(false);
    }

    public void disableButtons() {
        btnAddArtist.setDisable(true);
        btnDeleteArtist.setDisable(true);
        btnUpdateArtist.setDisable(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     *
     */
    public void initialize() {
        tblLogin.setCellValueFactory(new PropertyValueFactory<>("tblLogin"));
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("tbEmail"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("tblName"));
        tblLastaccess.setCellValueFactory(new PropertyValueFactory<>("tblLastaccess"));
        musicGenre.setCellValueFactory(new PropertyValueFactory<>("musicGenre"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("tblStatus"));
        //add your data to the table here.
        tbData.setItems(tableModel);
    }

    // add your data here from any source 
    private ObservableList<Artist> tableModel = FXCollections.observableArrayList();

    /**
     *
     * @param e
     */
    public void handle(ActionEvent e) {
        String status;
        if (btnE.isSelected()) {
            status = "ENABLED";
        } else {
            status = "DISABLED";
        }
        Artist artist = new Artist(
                txtUserNameArtist.getText(),
                txtEmailArtist.getText(),
                txtFullNameArtist.getText(),
                datePicker.getValue(),
                choiceBox.getValue().toString(),
                status);

        tableModel.add(artist);

        txtFullNameArtist.clear();
        txtUserNameArtist.clear();
        txtEmailArtist.clear();

    }

}
