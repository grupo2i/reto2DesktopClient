/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
import org.junit.Test;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import reto2desktopclient.model.Artist;
import reto2desktopclient.view.ArtistManagementController;

/**
 *
 * @author Matteo Fernández
 */
public class ArtistManagementControllerTest extends ApplicationTest {

    private TableView tableArtist;
    private TextField txtUserNameArtist;
    private TextField txtEmailArtist;

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    /**
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/reto2desktopclient/view/ArtistManagement.fxml"));
        Parent root = (Parent) loader.load();
        ArtistManagementController controller;
        controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        tableArtist = lookup("#tableArtist").queryTableView();
        txtUserNameArtist = lookup("#txtUserNameArtist").query();
        txtEmailArtist = lookup("#txtEmailArtist").query();
    }

    /**
     * Test the initial stage
     */
    @Test
    public void test1_InitialState() {
        verifyThat("#btnUpdateArtist", isDisabled());
        verifyThat("#btnDeleteArtist", isDisabled());
        verifyThat("#btnAddArtist", isDisabled());
        //verifyThat("#choiceBox", Predicate.isEqual("POP"));
        verifyThat("#txtUserNameArtist", isEnabled());
        verifyThat("#txtFullNameArtist", isFocused());
        verifyThat("#txtFullNameArtist", isEnabled());
        verifyThat("#txtEmailArtist", isEnabled());
        verifyThat("#lblNameError1", isInvisible());
        verifyThat("#lblEmailError1", isInvisible());
        verifyThat("#lblUsernameError1", isInvisible());
        //  verifyThat("#adminMenuController", isEnabled());
        //data loads correctly
        int rowCount;
        rowCount = lookup("#tableArtist").queryTableView().getItems().size();
        assertNotEquals(rowCount, 0);
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull(row);
    }

    /**
     * Test when the table is selected
     */
    @Test
    public void test2_tableIsSelected() {
        int rowCount = tableArtist.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and select it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        //get selected item and index from table
        Artist selectedArtist;
        selectedArtist = (Artist) tableArtist.getSelectionModel()
                .getSelectedItem();
        int selectedIndex;
        selectedIndex = tableArtist.getSelectionModel().getSelectedIndex();
        verifyThat("#txtFullNameArtist", hasText(selectedArtist.getFullName()));
        verifyThat("#txtEmailArtist", hasText(selectedArtist.getEmail()));
        verifyThat("#txtUserNameArtist", hasText(selectedArtist.getLogin()));
        //  verifyThat("#txtStatus", hasText(selectedArtist.getUserStatus().toString()));
        verifyThat("#btnAddArtist", isDisabled());
        verifyThat("#btnUpdateArtist", isEnabled());
        verifyThat("#btnDeleteArtist", isEnabled());
        //deselect row
        press(KeyCode.CONTROL);
        clickOn(row);
        release(KeyCode.CONTROL);
        verifyThat("#btnAddArtist", isDisabled());
        verifyThat("#btnUpdateArtist", isDisabled());
        verifyThat("#btnDeleteArtist", isDisabled());
    }

    /**
     * Test when you try to update the artist
     */
    @Test
    public void test3_UpdateArtist() {
        int rowCount;
        String existingArtistUser;
        String existingArtistEmail;
        String secondArtistEmail;
        String secondArtistUser;
        rowCount = tableArtist.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        //look for 1st row in table view and click it
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        Artist selectedArtist = (Artist) tableArtist.getSelectionModel().getSelectedItem();
        existingArtistUser = selectedArtist.getLogin();
        existingArtistEmail = selectedArtist.getEmail();

        Node secondRow = lookup(".table-row-cell").nth(1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(secondRow);
        Artist secondArtist = (Artist) tableArtist.getSelectionModel().getSelectedItem();

        secondArtistUser = secondArtist.getLogin();
        secondArtistEmail = secondArtist.getEmail();

        clickOn("#txtUserNameArtist");
        eraseText(secondArtist.getLogin().length());
        write(existingArtistUser);
        clickOn("#btnUpdateArtist");
        //hacerlo visible xq no esta
        verifyThat("#lblUsernameError1", isVisible());

        clickOn("#txtUserNameArtist");
        eraseText(existingArtistUser.length());
        String randomUser = "username" + new Random().nextInt();
        write(randomUser);

        doubleClickOn("#txtEmailArtist");
        eraseText(secondArtist.getEmail().length());
        write(existingArtistEmail);
        clickOn("#btnUpdateArtist");
        //hacerlo visible xq no esta
        verifyThat("#lblEmailError1", isVisible());

        doubleClickOn("#txtEmailArtist");
        eraseText(existingArtistEmail.length());
        String randomEmail = "email" + new Random().nextInt() + "@mail.com";
        write(randomEmail);

        clickOn("#btnUpdateArtist");
        clickOn(row);
        Artist selectedArtistVerify;
        selectedArtistVerify = (Artist) tableArtist.getSelectionModel().getSelectedItem();
        String existingArtistUserVerify = selectedArtist.getLogin();
        String existingArtistEmailVerify = selectedArtist.getEmail();
        assertNotEquals("Logins do not match", existingArtistUserVerify, randomUser);
        assertNotEquals("Emails do not match", existingArtistEmailVerify, randomEmail);
    }

    /**
     * Test when you delete the artist
     */
    @Test
    public void test4_DeleteArtist() {
        //get row count
        int rowNumber = tableArtist.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowNumber, 0);
        //look for 1st row in table view and click it
        Node row;
        row = lookup(".table-row-cell").nth(tableArtist.getItems().size() - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnDeleteArtist", isEnabled());
        clickOn("#btnDeleteArtist");
        verifyThat("Are you sure you want to delete the selected Artist?\n"
                + "This option can't be reversed.",
                isVisible());
        clickOn(isDefaultButton());
        tableArtist.refresh();
        verifyThat("#btnDeleteArtist", isDisabled());
        assertEquals("The row has not been deleted!!!",
                rowNumber - 1, tableArtist.getItems().size());
    }

    /**
     * Checks the email
     */
    @Test
    public void test5_CheckEMAIL() {
        //Error is enabled when the format is incorrect.
        clickOn("#txtEmailArtist");
        write("12345");
        verifyThat("* Must match: a@a.aa", isVisible());

        //Buttons are disabled when the text field is empty.
        clickOn("#txtEmailArtist");
        write("");
        verifyThat("#lblEmailError1", isVisible());
        verifyThat("#btnAddArtist", isDisabled());
        verifyThat("#btnUpdateArtist", isDisabled());
        verifyThat("#btnDeleteArtist", isDisabled());
        verifyThat("* Field must not be empty", isVisible());

        //Error is enabled when the text is long.
        clickOn("#txtEmailArtist");
        write(OVERSIZED_TEXT);
        verifyThat("#lblEmailError1", isVisible());
        verifyThat("* Must be < than 255", isVisible());
    }

    /**
     * Check the fullname
     */
    @Test
    public void test6_CheckFullName() {
        //Error is enabled when the text is long.
        clickOn("#txtFullNameArtist");
        write(OVERSIZED_TEXT);
        verifyThat("* Must be < than 255", isVisible());

        //Error is enabled when the format is incorrect.
        clickOn("#txtFullNameArtist");
        write("12345");
        verifyThat("#lblNameError1", isVisible());

        //Buttons are disabled when the text field is empty.
        doubleClickOn("#txtFullNameArtist");
        eraseText(1);
        verifyThat("* Field must not be empty", isVisible());
        verifyThat("#btnAddArtist", isDisabled());
        verifyThat("#btnUpdateArtist", isDisabled());
        verifyThat("#btnDeleteArtist", isDisabled());

        //Error is enabled when the text is too short.
        clickOn("#txtFullNameArtist");
        write("a");
        eraseText(1);
        verifyThat("#lblNameError1", isVisible());
    }

    /**
     * Check the username
     */
    @Test
    public void test6_CheckUserName() {
        //USERNAME
        //Error is enabled when the text is long.
        clickOn("#txtUserNameArtist");
        write(OVERSIZED_TEXT);
        verifyThat("#lblUsernameError1", isVisible());

        //Error is enabled when the format is incorrect.
        clickOn("#txtUserNameArtist");
        write("12345");
        verifyThat("#lblUsernameError1", isVisible());

        //Buttons are disabled when the text field is empty.
        clickOn("#txtUserNameArtist");
        write("");
        verifyThat("#lblUsernameError1", isVisible());
        verifyThat("#btnAddArtist", isDisabled());
        verifyThat("#btnUpdateArtist", isDisabled());
        verifyThat("#btnDeleteArtist", isDisabled());

        //Error is enabled when the text is too short.
        clickOn("#txtUserNameArtist");
        write("a");
        eraseText(1);
        verifyThat("#lblUsernameError1", isVisible());
    }
}
