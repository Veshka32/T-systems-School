package UItest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeleniumITReal {

    private static SeleniumPerformer performer;
    private static String homePage = "http://localhost:8080/mobile";

    @Test
    void test() {

        int status = 400;
        while (status != 200) {
            try {
                URL url = new URL(homePage);
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                status = connection.getResponseCode();
            } catch (IOException e) {
            }
        }

        performer = new SeleniumPerformer();
        performer.openHomePage(homePage);

            assertNotNull(performer.findElementByLink("SIGN UP"));
            //go to sign up page
            performer.clickLink("SIGN IN");

            performer.fieldForm("login", "9990000007");
            performer.fieldForm("password", "test");
            performer.submit(performer.findElementByClass("btn-success"));

            assertNotNull(performer.findElementByLink("Cabinet"));
            assertNotNull(performer.findElementByLink("LOG OUT"));

            //go to manage cabinet->options
            performer.clickLink("Cabinet");
            assertEquals(performer.getTitle(), "User cabinet");
            WebElement element=performer.findElementsByPath("//*[@id=\"tariffOption7\"]/div[2]").get(0);
            assertEquals(element.getText(),"Can't be deactivated");
            element=performer.findElementByClass("btn-danger");
            assertNotNull(element);
            assertEquals(element.getAttribute("value"), "Delete");

            //check tariffs
            performer.clickLink("Tariffs");
            List<WebElement> tariffs=performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]/div");
            assertEquals(tariffs.size(),3);
            performer.clickButton("moreTariffs");
            tariffs=performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]/div");
            assertEquals(tariffs.size(),4);

            //check available options
            performer.clickLink("Available options");
            List<WebElement> options=performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]/div");
            assertEquals(options.size(),3);
            performer.clickButton("moreOptions");
            tariffs=performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]/div");
            assertEquals(tariffs.size(),6);

            //block number
            performer.clickLink("Block number");
        assertNotNull(performer.findElementByClass("badge-danger")); //block badge
        assertTrue(performer.findElementById("tab1").getAttribute("class").contains("disabled"));

        //unblock number
        performer.clickLink("Unblock number");
        assertFalse(performer.findElementById("tab1").getAttribute("class").contains("disabled"));
    }

}
