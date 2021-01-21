/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;

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

    }

    @Test
    public void testA_InitialState() {
        verifyThat("#lblError", isInvisible());
        verifyThat("#btnRemove", isDisabled());
        verifyThat("#btnAdd", isEnabled());
    }
    
    @Test
    public void testB_TableDataLoaded() {
    }
    
    @Test
    public void testC_ModifyEventName() {
    }
    
    @Test
    public void testD_ModifyEventDate() {
    }
    
    @Test
    public void testE_ModifyEventPlace() {
    }
    
    @Test
    public void testF_ModifyEventPrice() {
    }
    
    @Test
    public void testG_ModifyEventDescription() {
    }
    
    @Test
    public void testH_AddEvent() {
    }
    
    @Test
    public void testI_RemoveEvent() {
        //Verify the alert too
    }
    
    @Test
    public void testJ_MenuChangeWindow() {
    }
    
    @Test
    public void testK_EmptyStrings() {
    }
    
    @Test
    public void testL_MaxLengthStrings() {
    }
    
    @Test
    public void testM_DateFormat() {
        //Date format is dd/mm/yy
    }
    
    @Test
    public void testN_PriceNonNegative() {
        //Prices cannot be negative
    }
}
