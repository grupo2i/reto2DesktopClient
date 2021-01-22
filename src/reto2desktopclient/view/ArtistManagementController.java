package reto2desktopclient.view;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ArtistManagerFactory;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.exceptions.UnexpectedErrorException;
import reto2desktopclient.exceptions.UserInputException;
import reto2desktopclient.model.Artist;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.UserStatus;

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
    private TableView<Artist> tableArtist;
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
    @FXML
    ToggleGroup group = new ToggleGroup();
    private ObservableList artistData;
    @FXML
    private AdminMenuController adminMenuController;

    boolean errorEmailLenght = true;
    boolean errorEmailPattern = true;
    boolean errorTxtUserNameArtist = true;
    boolean errorTxtFullNameArtist = true;
    boolean tableIsSelected = false;

    /**
     *
     * @return
     */
    public static final LocalDate NOW_LOCAL_DATE() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    /**
     *
     * @param root
     */
    public void initStage(Parent root
    ) {
        //Initialize the stage
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Artist Management");
        stage.setResizable(false);
        stage.show();
        adminMenuController.setStage(stage);
        initializebottonGroup();
        enableButtons();
        initializeCheckBox();

        lblNameError1.setVisible(false);
        lblEmailError1.setVisible(false);
        lblUsernameError1.setVisible(false);
        txtEmailArtist.textProperty().addListener(this::handletxtEmailArtist);
        txtFullNameArtist.textProperty().addListener(this::handleTextFullNameArtist);
        txtUserNameArtist.textProperty().addListener(this::handletxtUserNameArtist);

        //Stablishing cell value factories on table columns
        tblLogin.setCellValueFactory(new PropertyValueFactory<>("tblLogin"));
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("tbEmail"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("tblName"));
        tblLastaccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
        musicGenre.setCellValueFactory(new PropertyValueFactory<>("musicGenre"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        //Creating an Artist observable List with all registered artist.
        artistData = FXCollections.observableList(ArtistManagerFactory
                .getArtistManager().getAllArtists(new GenericType<List<Artist>>() {
                }));
        //Setting the table model to the observable list above.
        tableArtist.setItems(artistData);
        //Setting table selection listener.
        tableArtist.getSelectionModel().selectedItemProperty()
                .addListener(this::handleArtistTableSelectionChange);

        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Showing stage...");
    }

    @FXML
    public void handleButtonDelete(ActionEvent event) throws UserInputException {
        try {
            //Get selected club data from table view model
            Artist selectedArtist = ((Artist) tableArtist.getSelectionModel().getSelectedItem());
            //Ask user for confirmation on delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected Artist?\n"
                    + "This option can't be reversed.",
                    ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            //If OK to deletion
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //delete user from server side
                ArtistManagerFactory.getArtistManager().remove(selectedArtist.getId().toString());
                //clears editing fields
                resetFieldsAndLabels();
                //Clear selection and refresh table view 
                tableArtist.getSelectionModel().clearSelection();
                artistData = FXCollections.observableList(ArtistManagerFactory
                        .getArtistManager().getAllArtists(new GenericType<List<Artist>>() {
                        }));
                tableArtist.setItems(artistData);
                LOGGER.log(Level.INFO, "Artist was deleted succesfuly");
            }
        } catch (ClientErrorException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    @FXML
    public void handleButtonUpdate(ActionEvent event) throws UserInputException {
        Artist selectedArtist = ((Artist) tableArtist.getSelectionModel().getSelectedItem());
        handletxtUserNameArtist(artistData);
        handletxtEmailArtist(artistData);
        handleTextFullNameArtist(artistData);

        selectedArtist.setFullName(txtFullNameArtist.getText());
        selectedArtist.setEmail(txtEmailArtist.getText());
        selectedArtist.setLogin(txtUserNameArtist.getText());
        ArtistManagerFactory.getArtistManager().edit(selectedArtist);
    }

    /**
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     *
     * @param primaryStage
     */
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void handleArtistTableSelectionChange(ObservableValue observable,
            Object oldVaue, Object newValue) {
        tableIsSelected = true;
        if (newValue != null) {//A row of the table is selected.
            //Enable See Events, delete and update buttons.
            enableButtons();
            Artist selectedArtist = ((Artist) tableArtist.getSelectionModel().getSelectedItem());
            txtEmailArtist.setText(selectedArtist.getEmail());
            txtFullNameArtist.setText(selectedArtist.getFullName());
            txtUserNameArtist.setText(selectedArtist.getLogin());
            choiceBox.setValue(selectedArtist.getMusicGenre().getValue());
            group.setUserData(selectedArtist.getTblStatus());
            tableIsSelected = true;
        } else { //There isn't any row selected.
            //Disable all buttons.
            disableButtons();
            resetFieldsAndLabels();
            tableIsSelected = false;
        }
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

    /**
     *
     */
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

    /**
     *
     */
    public void enableButtons() {
        btnAddArtist.setDisable(false);
        btnDeleteArtist.setDisable(false);
        btnUpdateArtist.setDisable(false);
    }

    /**
     *
     */
    public void disableButtons() {
        btnAddArtist.setDisable(true);
        btnDeleteArtist.setDisable(true);
        btnUpdateArtist.setDisable(true);
    }

    /**
     *
     * @param e
     * @throws reto2desktopclient.exceptions.UnexpectedErrorException
     */
    public void handleButtonAdd(ActionEvent e) throws UnexpectedErrorException {
        Artist artist = new Artist();
        artist.setFullName(txtFullNameArtist.getText());
        artist.setEmail(txtEmailArtist.getText());
        ArtistManagerFactory.getArtistManager().create(artist);
        artistData = FXCollections.observableList(ArtistManagerFactory
                .getArtistManager().getAllArtists(new GenericType<List<Artist>>() {
                }));
        tableArtist.setItems(artistData);
        LOGGER.log(Level.INFO, "Artist was added succesfuly");
        resetFieldsAndLabels();
    }

    private void resetFieldsAndLabels() {
        txtFullNameArtist.clear();
        txtUserNameArtist.clear();
        txtEmailArtist.clear();
    }

}
