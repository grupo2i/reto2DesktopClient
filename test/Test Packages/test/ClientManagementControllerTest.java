package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import reto2desktopclient.model.Client;
import reto2desktopclient.model.UserPrivilege;
import reto2desktopclient.model.UserStatus;
import reto2desktopclient.view.ClientManagementController;

/**
 * Tests Client Management controller.
 * 
 * @author Aitor Fidalgo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientManagementControllerTest extends ApplicationTest {
    
    /**
     * Oversized text to test overflow error.
     */
    private static final String OVERSIZED_TEXT="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+
                                               "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    
    private TableView table;
    private Label lblInputError;
    
    
    /**
     * Starts application to be tested.
     *
     * @param stage Primary Stage object
     * @throws Exception if we have any error
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/ClientManagement.fxml"));
        Parent root = (Parent) loader.load();
        //Getting window controller.
        ClientManagementController controller = (loader.getController());
        controller.setStage(stage);
        //Initializing stage.
        controller.initStage(root);
        
        table = lookup("#tableClients").queryTableView();
        lblInputError = lookup("#lblInputError").query();
    }
    
    
    @Test
    //@Ignore
    public void testB_InitialState() {
        verifyThat("#btnNewClient", isFocused());
        verifyThat("#btnNewClient", isEnabled());
        verifyThat("#btnSeeEvents", isDisabled());
        verifyThat("#lblInputError", isInvisible());
        //TODO: Verify that menuItemSeeEvents is disabled.
    }
    
    @Test
    //@Ignore
    public void testC_ClientCreation() {
        int rowNumber = table.getItems().size();
        clickOn("#btnNewClient");
        assertEquals(rowNumber + 1, table.getItems().size());
        //Getting the new Client.
        Client client = (Client) table.getItems().get(0);
        assertEquals("The Client was not created with disabled status by default.",
                UserStatus.DISABLED, client.getUserStatus());
        assertEquals("The Client was not created with CLIENT privilege.",
                UserPrivilege.CLIENT, client.getUserPrivilege());
        assertNotNull("The Client was not created with the default password.",
                client.getPassword());
        assertNotNull("The Client was created without profile image.",
                client.getProfileImage());
    }
    
    /**
     * Tests that the table is updated when an edit commit happens.
     */
    @Test
    //@Ignore
    public void testD_ClientUpdate() {
        clickOn("#btnNewClient");
        updateCell(0, "alreadyRegisteredLogin");
        updateCell(1, "alredyRegistered@email.com");
        updateCell(2, "Already Registered Name");
        Node node = lookup(".table-cell").nth(4).query();
        doubleClickOn(node);
        push(KeyCode.DOWN);
        Client client = (Client) table.getItems().get(0);
        assertEquals("alreadyRegisteredLogin", client.getLogin());
        assertEquals("alredyRegistered@email.com", client.getEmail());
        assertEquals("Already Registered Name", client.getFullName());
        assertEquals(UserStatus.ENABLED, client.getUserStatus());
    }
    
    /**
     * Writes the specified text on the specified cell and makes an edit commit.
     * 
     * @param cellIndex Specified cell, nth() cell of the css class ".table-cell".
     * @param text The specified text.
     */
    private void updateCell(int cellIndex, String text) {
        Node node = lookup(".table-cell").nth(cellIndex).query();
        doubleClickOn(node);
        write(text);
        push(KeyCode.ENTER);
    }
    
    /**
     * Tests that errors when login column is edited are controled.
     * 
     * This method tests a 'login already registered' error that is why it is
     * important to have previously executed the test D or to have a registered
     * user with the login 'alreadyRegisteredLogin' in the database.
     */
    @Test
    //@Ignore
    public void testE_ClientLoginEditingErrors() {
        clickOn("#btnNewClient");
        Client client = (Client) table.getItems().get(0);
        
        //Testing empty field on column login...
        updateCell(0, "anyLogin");
        updateSelectedCell("");
        verifyThat("* Fields must not be empty", isVisible());
        //Testing old value is restored...
        assertEquals("anyLogin", client.getLogin());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing too long data on column login...
        updateSelectedCell(OVERSIZED_TEXT);
        verifyThat("* All data must be less than 255 characters.", isVisible());
        //Testing old value is restored...
        assertEquals("anyLogin", client.getLogin());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing already registered login...
        updateSelectedCell("alreadyRegisteredLogin");
        verifyThat("* Login is already registered.", isVisible());
        //Testing old value is restored...
        client = (Client) table.getItems().get(0);
        assertEquals("anyLogin", client.getLogin());
        //Testing error label is hiden...
        Node cell = lookup(".table-cell").nth(0).query();
        doubleClickOn(cell);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
    }
    
    /**
     * Writes the specifies text on the selected cell and makes an edit commit.
     * 
     * @param text The specified text.
     */
    private void updateSelectedCell(String text) {
        push(KeyCode.ENTER);
        push(KeyCode.BACK_SPACE);
        write(text);
        push(KeyCode.ENTER);
    }
    
    /**
     * Tests that errors when email column is edited are controled.
     * 
     * This method tests an 'email already registered' error that is why it is
     * important to have previously executed the test D or to have a registered
     * user with the email 'alredyRegistered@email.com' in the database.
     */
    @Test
    //@Ignore
    public void testF_ClientEmailEditingErrors() {
        clickOn("#btnNewClient");
        Client client = (Client) table.getItems().get(0);
        
        //Testing empty field on column email...
        updateCell(1, "any@email.com");
        updateSelectedCell("");
        verifyThat("* Fields must not be empty", isVisible());
        //Testing old value is restored...
        assertEquals("any@email.com", client.getEmail());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing too long data on column email...
        updateSelectedCell(OVERSIZED_TEXT);
        verifyThat("* All data must be less than 255 characters.", isVisible());
        //Testing old value is restored...
        assertEquals("any@email.com", client.getEmail());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing wrong pattern on column email...
        updateSelectedCell("wrongFormatEmail");
        verifyThat("* Email must match the following format:\n   jsmith@example.com", isVisible());
        //Testing old value is restored...
        assertEquals("any@email.com", client.getEmail());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing already registered email...
        updateSelectedCell("alredyRegistered@email.com");
        verifyThat("* Email is already registered.", isVisible());
        //Testing old value is restored...
        client = (Client) table.getItems().get(0);
        assertEquals("any@email.com", client.getEmail());
        //Testing error label is hiden...
        Node cell = lookup(".table-cell").nth(1).query();
        doubleClickOn(cell);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
    }
    
    /**
     * Tests that errors when full name column is edited are controled.
     */
    @Test
    //@Ignore
    public void testG_ClientFullNameEditingErrors() {
        clickOn("#btnNewClient");
        Client client = (Client) table.getItems().get(0);
        
        //Testing empty field on column full name...
        updateCell(2, "Any Full Name");
        updateSelectedCell("");
        verifyThat("* Fields must not be empty", isVisible());
        //Testing old value is restored...
        assertEquals("Any Full Name", client.getFullName());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
        
        //Testing too long data on column full name...
        updateSelectedCell(OVERSIZED_TEXT);
        verifyThat("* All data must be less than 255 characters.", isVisible());
        //Testing old value is restored...
        assertEquals("Any Full Name", client.getFullName());
        //Testing error label is hiden...
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat(lblInputError, isInvisible());
    }
    
    @Test
    //@Ignore
    public void testH_SeeEventsButtonEnableDisableOnSelectionChange() {
        Node cell;
        clickOn("#btnNewClient");
        cell = lookup(".table-cell").nth(0).query();
        verifyThat("#btnSeeEvents", isEnabled());
        press(KeyCode.CONTROL);
        clickOn(cell);
        release(KeyCode.CONTROL);
        verifyThat("#btnSeeEvents", isDisabled());
    }
    
}
