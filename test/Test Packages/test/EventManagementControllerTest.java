/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import reto2desktopclient.model.Event;
import reto2desktopclient.view.EventManagementController;

/**
 *
 * @author Martin Angulo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventManagementControllerTest extends ApplicationTest {
    /** Maximum length of the text field inputs. */
    private static final int MAX_TEXT_LENGTH = 255;
    /** String of size MAX_TEXT_LENGTH + 1 for testing. */
    private static String longString = "";
    
    enum colPos { colName, colDate, colPlace, colPrice, colDescription, colClub }
    
    /**
     * Initializes testing variables.
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void initialize() throws Exception {
        for(int i = 0; i <= MAX_TEXT_LENGTH; ++i)
            longString += "A";
    }
    
    /**
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/EventManagement.fxml"));
        Parent root = (Parent) loader.load();
        EventManagementController controller = (loader.getController());
        //ClubManagementController controller = (loader.getController());
        stage.setResizable(Boolean.FALSE);
        controller.setStage(stage);
        controller.initStage(root);
    }

    @Test
    public void testA_InitialState() {
        //Label
        verifyThat("#lblError", isInvisible());
        //Buttons
        verifyThat("#btnRemoveEvent", isVisible());
        verifyThat("#btnRemoveEvent", isEnabled());
        verifyThat("#btnAddEvent", isVisible());
        verifyThat("#btnAddEvent", isEnabled());
        verifyThat("#btnAddEvent",  (Button b) -> b.isFocused());
        //Table
        verifyThat("#tblEvents", isVisible());
        verifyThat("#colName", isVisible());
        verifyThat("#colDate", isVisible());
        verifyThat("#colPlace", isVisible());
        verifyThat("#colPrice", isVisible());
        verifyThat("#colDescription", isVisible());
    }
    
    @Test
    public void testB_TableDataLoaded() {
        int rowCount = lookup("#tblEvents").queryTableView().getItems().size();
        assertNotEquals(rowCount, 0);
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull(row);
    }
    
    @Test
    public void testC_AddEvent() {
        int rowCount = lookup("#tblEvents").queryTableView().getItems().size();
        clickOn("#btnAddEvent");
        assertNotEquals(rowCount, lookup("#tblEvents").queryTableView().getItems().size());
    }
    
    @Test
    public void testD_ModifyEvent() {
        int lastRow = lookup("#tblEvents").queryTableView().getItems().size() - 1;
        Node col = lookup(".table-cell").nth(colPos.colName.ordinal()).query();
        doubleClickOn(col);
        write("Name");
        push(KeyCode.ENTER);
        col = lookup(".table-cell").nth(colPos.colDate.ordinal()).query();
        doubleClickOn(col);
        write("15/12/12");
        push(KeyCode.ENTER);      
        col = lookup(".table-cell").nth(colPos.colPlace.ordinal()).query();
        doubleClickOn(col);
        write("Place");
        push(KeyCode.ENTER);
        col = lookup(".table-cell").nth(colPos.colPrice.ordinal()).query();
        doubleClickOn(col);
        write("100");
        push(KeyCode.ENTER);
        col = lookup(".table-cell").nth(colPos.colDescription.ordinal()).query();
        doubleClickOn(col);
        write("Description");
        push(KeyCode.ENTER);
        col = lookup(".table-cell").nth(colPos.colClub.ordinal()).query();
        doubleClickOn(col);
        clickOn(col);
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
    }
    
    @Test
    public void testE_ModifyEventName() {
        TableView tblEvents = lookup("#tblEvents").queryTableView();
        Node col = lookup(".table-cell").nth(colPos.colName.ordinal()).query();
        String preEdit = ((Event)tblEvents.getItems().get(0)).getName();
        doubleClickOn(col);
        write("anyname");
        push(KeyCode.ENTER);
        String postEdit = ((Event)tblEvents.getItems().get(0)).getName();
        assertNotEquals(preEdit, postEdit);
    }
    
    @Test
    public void testF_ModifyEventDate() {
        TableView tblEvents = lookup("#tblEvents").queryTableView();
        Node col = lookup(".table-cell").nth(colPos.colDate.ordinal()).query();
        String preEdit = ((Event)tblEvents.getItems().get(0)).getDate().toString();
        doubleClickOn(col);
        write("12/12/12");
        push(KeyCode.ENTER);
        String postEdit = ((Event)tblEvents.getItems().get(0)).getDate().toString();
        assertNotEquals(preEdit, postEdit);
    }
    
    @Test
    public void testG_ModifyEventPlace() {
        TableView tblEvents = lookup("#tblEvents").queryTableView();
        Node col = lookup(".table-cell").nth(colPos.colPlace.ordinal()).query();
        String preEdit = ((Event)tblEvents.getItems().get(0)).getPlace();
        doubleClickOn(col);
        write("anyplace");
        push(KeyCode.ENTER);
        String postEdit = ((Event)tblEvents.getItems().get(0)).getPlace();
        assertNotEquals(preEdit, postEdit);
    }
    
    @Test
    public void testH_ModifyEventPrice() {
        TableView tblEvents = lookup("#tblEvents").queryTableView();
        Node col = lookup(".table-cell").nth(colPos.colPrice.ordinal()).query();
        String preEdit = ((Event)tblEvents.getItems().get(0)).getTicketprice().toString();
        doubleClickOn(col);
        write("5");
        push(KeyCode.ENTER);
        String postEdit = ((Event)tblEvents.getItems().get(0)).getTicketprice().toString();
        assertNotEquals(preEdit, postEdit);
    }
    
    @Test
    public void testI_ModifyEventDescription() {
        TableView tblEvents = lookup("#tblEvents").queryTableView();
        Node col = lookup(".table-cell").nth(colPos.colDescription.ordinal()).query();
        String preEdit = ((Event)tblEvents.getItems().get(0)).getDescription();
        doubleClickOn(col);
        write("anydescription");
        push(KeyCode.ENTER);
        String postEdit = ((Event)tblEvents.getItems().get(0)).getDescription();
        assertNotEquals(preEdit, postEdit);
    }
    
    @Test
    public void testJ_EmptyStrings() {
        String emptyError = "* Field must not be empty.";
        //Name
        Node col = lookup(".table-cell").nth(colPos.colName.ordinal()).query();
        doubleClickOn(col);
        write("a");
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(emptyError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Date
        col = lookup(".table-cell").nth(colPos.colDate.ordinal()).query();
        doubleClickOn(col);
        write("a");
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(emptyError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Place
        col = lookup(".table-cell").nth(colPos.colPlace.ordinal()).query();
        doubleClickOn(col);
        write("a");
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(emptyError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Price
        col = lookup(".table-cell").nth(colPos.colPrice.ordinal()).query();
        doubleClickOn(col);
        write("a");
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(emptyError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Description
        col = lookup(".table-cell").nth(colPos.colDescription.ordinal()).query();
        doubleClickOn(col);
        write("a");
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(emptyError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
    }
    
    @Test
    public void testK_MaxLengthStrings() {
        String maxLengthError = "* Must be less than 255 characters.";
        //Name
        Node col = lookup(".table-cell").nth(colPos.colName.ordinal()).query();
        doubleClickOn(col);
        write(longString);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(maxLengthError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Place
        col = lookup(".table-cell").nth(colPos.colPlace.ordinal()).query();
        doubleClickOn(col);
        write(longString);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(maxLengthError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        //Description
        col = lookup(".table-cell").nth(colPos.colDescription.ordinal()).query();
        doubleClickOn(col);
        write(longString);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(maxLengthError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
    }
    
    @Test
    public void testL_DateFormat() {
        //Date format is mm/dd/yy
        String dateFormatError = "* Date must have format: dd/mm/yy\nand be possible.";
        Node col = lookup(".table-cell").nth(colPos.colDate.ordinal()).query();
        doubleClickOn(col);
        write("asdvgasfcdy");
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(dateFormatError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        col = lookup(".table-cell").nth(colPos.colDate.ordinal()).query();
        doubleClickOn(col);
        write("99/99/99");
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(dateFormatError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
    }
    
    @Test
    public void testM_PriceNonNegative() {
        //Prices cannot be negative
        String priceFormatError = "* Field should be a positive number.";
        Node col = lookup(".table-cell").nth(colPos.colPrice.ordinal()).query();
        doubleClickOn(col);
        write("-55");
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(priceFormatError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
        
        col = lookup(".table-cell").nth(colPos.colPrice.ordinal()).query();
        doubleClickOn(col);
        write("asdashjdh");
        push(KeyCode.ENTER);
        verifyThat("#lblError", isVisible());
        verifyThat(priceFormatError, isVisible());
        
        push(KeyCode.ENTER);
        push(KeyCode.ENTER);
        verifyThat("#lblError", isInvisible());
    }
    
    @Test
    public void testN_RemoveEvent() {
        //Verify the alert too
        int rowCount = lookup("#tblEvents").queryTableView().getItems().size();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btnRemoveEvent");
        verifyThat("Are you sure you want to delete an Event?", NodeMatchers.isVisible());
        clickOn("OK");
        assertNotEquals(rowCount, lookup("#tblEvents").queryTableView().getItems().size());
    }
}
