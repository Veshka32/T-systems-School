package UItest;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.DownConfig;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SeleniumIT {
    private static SeleniumPerformer performer;
    private static DockerCompose compose;
    private static String homePage = "http://192.168.99.100:8080/mobile";

    @BeforeAll
    public static void upDocker() {
        compose = DockerCompose.builder().absolute("C:\\Public\\mobileIS\\mobileIS\\src\\test\\resources\\docker-compose.yml").build();
        compose.up();
        compose.waitForCluster(2, TimeUnit.MINUTES);
    }

    @Test
    void test1_logIn() {

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

    @AfterAll
    public static void downDocker() {
        performer.closeWindow();
        compose.down(DownConfig.defaults().withRemoveVolumes(true));
        compose.down();
    }

    @Test
    void test2_option() {
        performer.clickLink("Options");
        assertEquals(performer.getTitle(), "Options");
        performer.clickLink("2");
        performer.clickLink("Next");

        //see option details
        performer.submit(performer.findElementByClass("btn-info"));
        assertEquals(performer.getTitle(), "Option");

        //edit option
        performer.submit(performer.findElementByClass("btn-warning"));
        assertEquals(performer.getTitle(), "Create option");

        //back to options
        performer.clickLink("Back to options");
        // Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Options");

        //create new
        performer.clickLink("Create new option");
        assertEquals(performer.getTitle(), "Create option");

        //try to create with errors
        performer.fieldForm("price", "-5");
        performer.scrollToElement(performer.findElementByClass("btn-success"));
        performer.submit(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Create option");
        assertNotNull(performer.findElementById("name.errors"));
        assertNotNull(performer.findElementById("price.errors"));

        //fill with values
        performer.fieldForm("name", "Test option");
        performer.fieldForm("price", "99.99");
        performer.fieldForm("cost", "10");
        performer.fieldForm("desc", "This is test option for presentation");
        performer.selectAll("bootstrap-duallistbox-nonselected-list_mandatory"); //select all mandatory
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
    void test3_client() {
        //find client by wrong number
        performer.clickLink("Clients");
        assertEquals(performer.getTitle(), "Clients");
        performer.fieldForm("phoneNumber", "9876543210");
        performer.submit(performer.findElementById("find"));
        WebElement element = performer.findElementById("message");
        assertNotNull(element);
        assertEquals(element.getText(), "there is no such client");
        assertEquals(performer.findElementById("result").getText(), ""); //result is empty

        //find client by wrong number format
        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "999");
        performer.submit(performer.findElementById("find"));
        element = performer.findElementById("message");
        assertNotNull(element);
        assertEquals(element.getText(), "must be 10 digits");

        //find client by existing number
        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "9990000002");
        performer.submit(performer.findElementById("find"));
        List<WebElement> list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        performer.fieldForm("passport", "2222222222");
        performer.submit(performer.findElementById("find1"));
        assertNotEquals(performer.findElementById("result").getText(), "");
        list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        //show client details
        performer.submit(performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr/td[7]/form/input[2]").get(0)); //pick from result

        //click create contract
        performer.submit(performer.findElementsByPath("/html/body/div/form[2]/input[2]").get(0));
        assertEquals(performer.getTitle(), "Edit contract");
        performer.selectAll("bootstrap-duallistbox-nonselected-list_optionsIds"); //select all options
        performer.submit(performer.findElementByClass("btn-success"));
        assertNotNull(performer.findElementByClass("bg-danger")); //error message
        performer.selectAll("bootstrap-duallistbox-selected-list_optionsIds"); //deselect all
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_optionsIds", "Hologram");
        performer.submit(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Contract"); //9990000007

        //log out
        performer.clickLink("LOG OUT");
        assertEquals(performer.getTitle(), "Multiverse Mobile");
    }

    @Test
    void test4_signUp() {
        assertNotNull(performer.findElementByLink("SIGN UP"));
        //go to sign up page
        performer.clickLink("SIGN UP");
        assertEquals(performer.getTitle(), "Sign up");

        //sign up wrong values
        performer.fieldForm("login", "9876543210");
        performer.fieldForm("password", "test");
        performer.submit(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Sign up");
        assertNotNull(performer.findElementById("error"));

        //sign up with right values
        performer.findElementById("login").clear();
        performer.findElementById("password").clear();
        performer.fieldForm("login", "9990000007");
        performer.fieldForm("password", "test");
        performer.submit(performer.findElementByClass("btn-success"));

        assertEquals(performer.getTitle(), "Sign in");
        performer.fieldForm("login", "9990000007");
        performer.fieldForm("password", "test");
        performer.submit(performer.findElementByClass("btn-success"));

        assertNotNull(performer.findElementByLink("Cabinet"));
        assertNotNull(performer.findElementByLink("LOG OUT"));
    }

    @Test
    void test5_userCabinet() {
        //go to manage cabinet->options
        performer.clickLink("Cabinet");
        assertEquals(performer.getTitle(), "User cabinet");
        WebElement element = performer.findElementsByPath("//*[@id=\"tariffOption7\"]/div[2]").get(0);
        assertEquals(element.getText(), "Can't be deactivated");
        element = performer.findElementByClass("btn-danger");
        assertNotNull(element);
        assertEquals(element.getText(), "Delete");

        //check tariffs
        performer.clickLink("Tariffs");
        List<WebElement> tariffs = performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]");
        assertEquals(tariffs.size(), 3);
        performer.clickButton("moreTariffs");
        tariffs = performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]");
        assertEquals(tariffs.size(), 4);

        //check available options
        performer.clickLink("Available options");
        List<WebElement> options = performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]");
        assertEquals(options.size(), 3);
        performer.clickButton("moreOptions");
        tariffs = performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]");
        assertEquals(tariffs.size(), 6);

        //block number
        performer.clickLink("Block number");
        assertNotNull(performer.findElementByClass("badge badge-pill badge-danger")); //block badge
        List<WebElement> tabs = performer.findElementsByClass("nav-link disabled");
        assertEquals(tabs.size(), 2); //to tabs must be disabled

        //unblock number
        performer.clickLink("Unlock number");
        assertNull(performer.findElementByClass("badge badge-pill badge-danger")); //block badge
        tabs = performer.findElementsByClass("nav-link disabled");
        assertEquals(tabs.size(), 0); //to tabs must be disabled
    }


}
