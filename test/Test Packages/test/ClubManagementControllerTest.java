package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
import reto2desktopclient.view.ClubManagementController;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClubManagementControllerTest extends ApplicationTest {
    /** Maximum length of the text field inputs. */
    private static final int MAX_TEXT_LENGTH = 255;
    /** String of size MAX_TEXT_LENGTH + 1 for testing. */
    private static String longString = "";
    
    /**
     * Initializes testing variables.
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void initialize() throws Exception {
        for(int i = 0; i <= MAX_TEXT_LENGTH; ++i)
            longString += "A";
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/reto2desktopclient/view/ClubManagement.fxml"));
        Parent root =(Parent)loader.load();
        ClubManagementController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root); 
    }
    
        /**
     * Tests the initial stage of the window.
     */
    @Test
    public void testA_initialState(){
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
    //@Test
    public void testB_fillAllData(){
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
    //@Test
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
    //@Test
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
   // @Test
    public void testE_shortPassword() {
        String minLenghtError= "* Field must not be empty";
        
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
     * Tests that the error message is shown when the email format
     * is incorrect.
     */
    //@Test
    public void testF_EmailFormat() {
        clickOn("#txtEmail");
        write("12345");
        verifyThat("* Must match: a@a.aa", isVisible());
    }
     /**
     * Tests that the error message is shown when the name format
     * is incorrect.
     */
    //@Test
    public void testF_NameFormat() {
        clickOn("#txtName");
        write("12345");
        verifyThat("* Must only contain letters", isVisible());
    }
     /**
     * Tests that the error message is shown when the phone number format
     * is incorrect.
     */
    //@Test
    public void testF_NameFormat() {
        clickOn("#txtName");
        write("12345");
        verifyThat("* Must only contain letters", isVisible());
    }
    
    
}
