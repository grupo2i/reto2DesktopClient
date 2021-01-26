package test;

import java.util.List;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.anything;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.base.WindowMatchers;
import static org.testfx.matcher.control.ButtonMatchers.isCancelButton;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;
import reto2desktopclient.model.Club;
import reto2desktopclient.view.ClubManagementController;

/**
 * test for club management window.
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClubManagementControllerTest extends ApplicationTest {

    int rowCount;
    private TextField txtLogin;
    private TextField txtEmail;
    private TableView clubTable;
    String newClubLogin;
    String newClubEmail;
    /**
     * Maximum length of the text field inputs.
     */
    private static final int MAX_TEXT_LENGTH = 255;
    /**
     * String of size MAX_TEXT_LENGTH + 1 for testing.
     */
    private static String longString = "";

    /**
     * Initializes testing variables.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void initialize() throws Exception {
        for (int i = 0; i <= MAX_TEXT_LENGTH; ++i) {
            longString += "A";
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/reto2desktopclient/view/ClubManagement.fxml"));
        Parent root = (Parent) loader.load();
        ClubManagementController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        clubTable = lookup("#clubTable").queryTableView();
        txtLogin = lookup("#txtLogin").query();
        txtEmail = lookup("#txtEmail").query();
    }

    /**
     * Tests the initial stage of the window.
     */
    @Test
    public void testA_initialState() {
        //Texts
        verifyThat("#txtLogin", hasText(""));
        verifyThat("#txtLogin", isVisible());
        verifyThat("#txtLogin", isEnabled());
        verifyThat("#txtLogin", (TextField t) -> t.isFocused());
        verifyThat("#lblErrorLogin", isInvisible());

        verifyThat("#txtName", hasText(""));
        verifyThat("#txtName", isVisible());
        verifyThat("#txtName", isEnabled());
        verifyThat("#lblErrorName", isInvisible());

        verifyThat("#txtEmail", hasText(""));
        verifyThat("#txtEmail", isVisible());
        verifyThat("#txtEmail", isEnabled());
        verifyThat("#lblErrorEmail", isInvisible());

        verifyThat("#txtLocation", hasText(""));
        verifyThat("#txtLocation", isVisible());
        verifyThat("#txtLocation", isEnabled());
        verifyThat("#lblErrorLocation", isInvisible());

        verifyThat("#txtPhoneNumber", hasText(""));
        verifyThat("#txtPhoneNumber", isVisible());
        verifyThat("#txtPhoneNumber", isEnabled());
        verifyThat("#lblErrorPhoneNumber", isInvisible());

        verifyThat("#txtStatus", hasText(""));
        verifyThat("#txtStatus", isVisible());
        verifyThat("#txtStatus  ", isEnabled());
        verifyThat("#lblErrorStatus", isInvisible());

        //Buttons
        verifyThat("#btnUpdate", isVisible());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnSeeEvents", isVisible());
        verifyThat("#btnSeeEvents", isDisabled());
        verifyThat("#btnDelete", isVisible());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnAdd", isVisible());
        verifyThat("#btnAdd", isDisabled());
    }

    /**
     * Tests the window when all data is set correctly.
     */
    @Test
    public void testB_fillAllData() {
        clickOn("#txtLogin");
        write("username");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
        clickOn("#txtEmail");
        write("email@email.com");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
        clickOn("#txtName");
        write("name");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
        clickOn("#txtLocation");
        write("erandio");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
        clickOn("#txtPhoneNumber");
        write("111222333");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
        clickOn("#txtStatus");
        write("ENABLED");
        verifyThat("#btnAdd", isEnabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
    }

    /**
     * Tests that the buttons is disabled when all text fields are empty.
     */
    @Test
    public void testC_emptyTexts() {
        clickOn("#txtLogin");
        write("");
        clickOn("#txtEmail");
        write("");
        clickOn("#txtName");
        write("");
        clickOn("#txtLocation");
        write("");
        clickOn("#txtPhoneNumber");
        write("");
        clickOn("#txtStatus");
        write("");
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
    }

    /**
     * Tests that the error labels are visible when the input texts are longer
     * than MAX_TEXT_LENGTH and that the buttons are disabled.
     */
    @Test
    public void testD_maxLengthText() {
        String maxLengthError = "* Must be < than 255";
        clickOn("#txtLogin");
        write(longString);
        verifyThat("#lblErrorLogin", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtEmail");
        write(longString);
        verifyThat("#lblErrorEmail", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtName");
        write(longString);
        verifyThat("#lblErrorName", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtLocation");
        write(longString);
        verifyThat("#lblErrorLocation", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtPhoneNumber");
        write(longString);
        verifyThat("#lblErrorPhoneNumber", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtStatus");
        write(longString);
        verifyThat("#lblErrorStatus", isVisible());
        verifyThat(maxLengthError, isVisible());
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
    }

    /**
     * Tests that the error message when the input is too short .
     */
    @Test
    public void testE_shortInput() {
        String minLenghtError = "* Field must not be empty";

        clickOn("#txtLogin");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorLogin", isVisible());
        verifyThat("* Field must not be empty", isVisible());
        clickOn("#txtEmail");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorEmail", isVisible());
        verifyThat(minLenghtError, isVisible());
        clickOn("#txtName");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorName", isVisible());
        verifyThat(minLenghtError, isVisible());
        clickOn("#txtLocation");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorLocation", isVisible());
        verifyThat(minLenghtError, isVisible());
        clickOn("#txtPhoneNumber");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorPhoneNumber", isVisible());
        verifyThat(minLenghtError, isVisible());
        clickOn("#txtStatus");
        write("q");
        eraseText(1);
        verifyThat("#lblErrorStatus", isVisible());
        verifyThat(minLenghtError, isVisible());
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());
    }

    /**
     * Tests that the error message is shown when the email format is incorrect.
     */
    @Test
    public void testF_EmailFormat() {
        clickOn("#txtEmail");
        write("12345");
        verifyThat("* Must match: a@a.aa", isVisible());
    }

    /**
     * Tests that the error message is shown when the name format is incorrect.
     */
    @Test
    public void testF_NameFormat() {
        clickOn("#txtName");
        write("12345");
        verifyThat("* Must only contain letters", isVisible());
    }

    /**
     * Tests that the error message is shown when the phone number format is
     * incorrect.
     */
    @Test
    public void testF_PhoneNumberFormat() {
        clickOn("#txtPhoneNumber");
        write("abcd");
        verifyThat("* Only numbers allowed", isVisible());
    }

    /**
     * Tests that the error message is shown when the status is not enabled or
     * disabled.
     */
    @Test
    public void testF_StatusFormat() {
        clickOn("#txtStatus");
        write("Enabledd");
        verifyThat("* Enter Enabled or Disabled", isVisible());
        eraseText(8);
        write("Disabledd");
        verifyThat("* Enter Enabled or Disabled", isVisible());
    }

    /**
     * Test user creation.
     */
    @Test
    public void testG_createUser() {
        //get row count
        rowCount = clubTable.getItems().size();
        String login = "username" + new Random().nextInt();
        String email = "email" + new Random().nextInt() + "@aa.com";
        clickOn("#txtLogin");
        write(login);
        clickOn("#txtEmail");
        write(email);
        clickOn("#txtName");
        write("nametest");
        clickOn("#txtLocation");
        write("locationtest");
        clickOn("#txtPhoneNumber");
        write("111222333");
        clickOn("#txtStatus");
        write("ENABLED");
        clickOn("#btnAdd");
        assertEquals("The row has not been added!!!", rowCount + 1, clubTable.getItems().size());
        Club club;
        club = (Club) clubTable.getItems().get(rowCount);
        assertEquals("Login does not match", club.getLogin(), login);
        assertEquals("Email does not match", club.getEmail(), email);
        assertEquals("Name does not match", club.getFullName(), "nametest");
        assertEquals("Location does not match", club.getLocation(), "locationtest");
        assertEquals("Phone number does not match", club.getPhoneNum(), "111222333");
        assertEquals("Status does not match", club.getUserStatus().toString(), "ENABLED");
    }

    /**
     * Test that an alert to confirm deletion appears and it does not delete
     * when cancel.
     */
    @Test
    public void testH_cancelOnDeleteUser() {
        //get row count
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and click it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");
        verifyThat("Are you sure you want to delete the selected club?\n"
                + "This option can't be reversed.",
                isVisible());
        clickOn(isCancelButton());
        assertEquals("A row has been deleted!!!", rowCount, clubTable.getItems().size());
    }

    /**
     * Test that user is deleted as ok button is clicked on confirmation dialog.
     */
    @Test
    public void testI_deleteUser() {
        //get row count
        int rowNumber = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowNumber, 0);
        //look for 1st row in table view and click it
        Node row = lookup(".table-row-cell").nth(clubTable.getItems().size() - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");
        verifyThat("Are you sure you want to delete the selected club?\n"
                + "This option can't be reversed.",
                isVisible());
        clickOn(isDefaultButton());
        clubTable.refresh();
        assertEquals("The row has not been deleted!!!",
                rowNumber - 1, clubTable.getItems().size());
    }

    /**
     * Tests user alert when username exists.
     */
    @Test
    public void testJ_clubExistingOnCreate() {
        //get row count
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //get an existing login from table data
        String login = ((Club) clubTable.getItems().get(0)).getLogin();
        //get random email
        String email = "email" + new Random().nextInt() + "@aa.com";
        //write that login on text field
        clickOn("#txtLogin");
        write(login);
        clickOn("#txtEmail");
        write(email);
        clickOn("#txtName");
        write("nametest");
        clickOn("#txtLocation");
        write("locationtest");
        clickOn("#txtPhoneNumber");
        write("111222333");
        clickOn("#txtStatus");
        write("ENABLED");
        clickOn("#btnAdd");
        verifyThat("Login username already exists", isVisible());
        clickOn(isDefaultButton());
        assertEquals("A row has been added!!!", rowCount, clubTable.getItems().size());
    }

    /**
     * Test user modification.
     */
    @Test
    public void testK_modifyClub() {
        //get row count
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and click it
        Node row = lookup(".table-row-cell").nth(clubTable.getItems().size() - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        //get selected item and index from table
        Club selectedClub = (Club) clubTable.getSelectionModel()
                .getSelectedItem();
        int selectedIndex = clubTable.getSelectionModel().getSelectedIndex();

        newClubLogin = "username" + new Random().nextInt();
        clickOn("#txtLogin");
        eraseText(selectedClub.getLogin().length());
        write(newClubLogin);

        newClubEmail = "email" + new Random().nextInt() + "@aa.com";;
        clickOn("#txtEmail");
        eraseText(selectedClub.getEmail().length());
        write(newClubEmail);

        clickOn("#btnUpdate");
        assertEquals("Login does not match", selectedClub.getLogin(), newClubLogin);
        assertEquals("Email does not match", selectedClub.getEmail(), newClubEmail);
        assertEquals("A row has been added and it shouldn't!!!", rowCount, clubTable.getItems().size());
    }

    /**
     * Test user modification with existing data.
     */
    @Test
    public void testK_modifyClubWithExistingData() {
        //get row count
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and click it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        Club selectedClub = (Club) clubTable.getSelectionModel().getSelectedItem();
        String existingClubLogin = selectedClub.getLogin();
        String existingClubEmail = selectedClub.getEmail();

        Node secondRow = lookup(".table-row-cell").nth(1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(secondRow);
        Club secondClub = (Club) clubTable.getSelectionModel().getSelectedItem();
        String secondClubLogin = secondClub.getLogin();
        String secondClubEmail = secondClub.getEmail();

        clickOn("#txtLogin");
        eraseText(secondClub.getLogin().length());
        write(existingClubLogin);
        clickOn("#btnUpdate");
        verifyThat("Login username already exists", isVisible());
        clickOn(isDefaultButton());

        clickOn("#txtLogin");
        eraseText(existingClubLogin.length());
        String randomUser = "username" + new Random().nextInt();
        write(randomUser);

        doubleClickOn("#txtEmail");
        eraseText(secondClub.getEmail().length());
        write(existingClubEmail);
        clickOn("#btnUpdate");
        verifyThat("Email already in use", isVisible());
        clickOn(isDefaultButton());

        doubleClickOn("#txtEmail");
        eraseText(existingClubEmail.length());
        String randomEmail = "email" + new Random().nextInt() + "@aa.com";
        write(randomEmail);

        clickOn("#btnUpdate");
        clickOn(row);
        Club selectedClubVerify = (Club) clubTable.getSelectionModel().getSelectedItem();
        String existingClubLoginVerify = selectedClub.getLogin();
        String existingClubEmailVerify = selectedClub.getEmail();
        assertNotEquals("Logins do not match", existingClubLoginVerify, randomUser);
        assertNotEquals("Emails do not match", existingClubEmailVerify, randomEmail);
    }

    /**
     * Test club table row selection.
     */
    @Test
    public void testL_tableSelection() {
        //get row count
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and select it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        //get selected item and index from table
        Club selectedClub = (Club) clubTable.getSelectionModel()
                .getSelectedItem();
        int selectedIndex = clubTable.getSelectionModel().getSelectedIndex();
        //assertions
        verifyThat("#txtLogin", hasText(selectedClub.getLogin()));
        verifyThat("#txtEmail", hasText(selectedClub.getEmail()));
        verifyThat("#txtName", hasText(selectedClub.getFullName()));
        verifyThat("#txtLocation", hasText(selectedClub.getLocation()));
        verifyThat("#txtPhoneNumber", hasText(selectedClub.getPhoneNum()));
        verifyThat("#txtStatus", hasText(selectedClub.getUserStatus().toString()));
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isEnabled());
        verifyThat("#btnDelete", isEnabled());
        verifyThat("#btnSeeEvents", isEnabled());
        //deselect row
        press(KeyCode.CONTROL);
        clickOn(row);
        release(KeyCode.CONTROL);
        //assertions
        verifyThat("#txtLogin", hasText(""));
        verifyThat("#txtEmail", hasText(""));
        verifyThat("#txtName", hasText(""));
        verifyThat("#txtLocation", hasText(""));
        verifyThat("#txtPhoneNumber", hasText(""));
        verifyThat("#txtStatus", hasText(""));
        verifyThat("#btnAdd", isDisabled());
        verifyThat("#btnUpdate", isDisabled());
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnSeeEvents", isDisabled());

    }

    /**
     * Test see events window opens.
     */
    @Test
    public void testM_SeeEventsWindow() {
        int rowCount = clubTable.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and select it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        clickOn("#btnSeeEvents");
        verifyThat(window("Event Management"), WindowMatchers.isShowing());
    }
}
