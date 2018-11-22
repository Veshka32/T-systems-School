import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import seleniumConfig.SeleniumPerformer;

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
        //Thread.sleep(3000);

        //go to sign in page
        performer.clickLink("SIGN IN");
        assertEquals(performer.getTitle(), "Sign in");
        //Thread.sleep(3000);

        //input in sign in wrong values
        performer.fieldForm("login", "badGuy");
        performer.fieldForm("password", "test");
        //Thread.sleep(3000);
        performer.submit(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Sign in");
        assertNotNull(performer.findElementById("error"));
        //Thread.sleep(3000);

        //input in sign with right values
        performer.fieldForm("login", "stas");
        performer.fieldForm("password", "test");
        //Thread.sleep(3000);
        performer.submit(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Universe mobile");
        assertNotNull(performer.findElementByLink("MANAGEMENT"));
        assertNotNull(performer.findElementByLink("LOG OUT"));
        //go to manage cabinet->options
        performer.clickLink("MANAGEMENT");
        assertEquals(performer.getTitle(), "Manager cabinet");
    }

    @Test
    void optionTest() {

        //Thread.sleep(3000);
        performer.clickLink("Options");
        assertEquals(performer.getTitle(), "Options");
        //Thread.sleep(3000);
        performer.clickLink("2");
        //Thread.sleep(3000);
        performer.clickLink("Next");
        //Thread.sleep(3000);

        //see option details
        performer.submit(performer.findElementByClass("btn-info"));
        //Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Option");
        //Thread.sleep(3000);

        //edit option
        performer.submit(performer.findElementByClass("btn-warning"));
        //Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Create option");

        //back to options
        performer.clickLink("Back to options");
        // Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Options");

        //create new
        performer.clickLink("Create new option");
        //Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Create option");

        //try to create empty
        performer.scrollToElement(performer.findElementByClass("btn-success"));
        //Thread.sleep(3000);
        performer.submit(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Create option");
        assertNotNull(performer.findElementById("name.errors"));
        assertNotNull(performer.findElementById("price.errors"));
        assertNotNull(performer.findElementById("subscribeCost.errors"));

        //fill with values
        performer.fieldForm("name", "Test option");
        performer.fieldForm("price", "99.99");
        performer.fieldForm("cost", "10");
        performer.fieldForm("desc", "This is test option for presentation");
        //Thread.sleep(3000);
        performer.selectAll("bootstrap-duallistbox-nonselected-list_mandatory"); //select all mandatory
        ////Thread.sleep(3000);
        performer.submit(performer.findElementByClass("btn-success"));
        assertNotNull(performer.findElementByClass("bg-danger")); //error message
        performer.selectAll("bootstrap-duallistbox-selected-list_mandatory");
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_mandatory", "Hologram");
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_mandatory", "Local network");
        performer.submit(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Option");//success create
        //performer.closeWindow();
    }

    @Test
    void contractTest() {
        performer.clickLink("Contracts");
        assertEquals(performer.getTitle(), "Contracts");
    }

    @Test
    void clientTest() {
        performer.clickLink("Clients");
        assertEquals(performer.getTitle(), "Clients");
        performer.fieldForm("phoneNumber", "9876543210");
        performer.submit(performer.findElementById("find"));
        WebElement element = performer.findElementById("message");
        assertNotNull(element);
        assertEquals(element.getAttribute("innerText"), "there is no such client");
        assertEquals(performer.findElementById("result").getText(), ""); //result is empty
        //*[@id="result"]/table

        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "999");
        performer.submit(performer.findElementById("find"));
        element = performer.findElementById("message");
        assertNotNull(element);
        assertEquals(element.getText(), "must be 10 digits");

        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "9990000011");
        performer.submit(performer.findElementById("find"));
        assertNotEquals(performer.findElementById("result").getText(), "");
        List<WebElement> list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "9990000006");
        performer.submit(performer.findElementById("find"));
        assertNotEquals(performer.findElementById("result").getText(), "");
        list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        //show client details
        performer.submit(performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr/td[7]/form/input[2]").get(0)); //pick from result
    }
}