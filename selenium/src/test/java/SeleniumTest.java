import com.SeleniumPerformer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeleniumTest {

    private static SeleniumPerformer performer;

    @BeforeAll
    public static void setup() {
        performer = new SeleniumPerformer();
        performer.openWindow();
        assertNotNull(performer.findElementByLink("SIGN IN"));
        assertNotNull(performer.findElementByLink("SIGN UP"));

        //go to sign in page
        performer.clickLink("SIGN IN");
        assertEquals(performer.getTitle(), "Sign in");

        //input in sign in wrong values
        performer.fieldForm("login", "badGuy");
        performer.fieldForm("password", "test");
        performer.submit(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Sign in");
        assertNotNull(performer.findElementById("error"));

        //input in sign with right values
        performer.fieldForm("login", "stas");
        performer.fieldForm("password", "test");
        performer.submit(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Multiverse mobile");
        assertNotNull(performer.findElementByLink("MANAGEMENT"));
        assertNotNull(performer.findElementByLink("LOG OUT"));
        //go to manage cabinet->options
        performer.clickLink("MANAGEMENT");
        assertEquals(performer.getTitle(), "Manager cabinet");
    }


}
