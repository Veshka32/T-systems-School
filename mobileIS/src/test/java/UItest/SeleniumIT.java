package UItest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

class SeleniumIT {
    private static SeleniumPerformer performer;
    private static String homePage = "http://192.168.99.100:8080/mobile";
    private static DockerRunner dockerRunner = new DockerRunner();

    @BeforeAll
    public static void upDocker() throws IOException, InterruptedException {
        dockerRunner.run("src\\test\\resources");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        CountDownLatch downLatch = new CountDownLatch(1);
        Callable<Integer> request = () -> {
            URL url = new URL(homePage);
            try {
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                return connection.getResponseCode();
            } catch (IOException e) {
                return 400;
            }
        };

        final int[] statuses = new int[1];

        scheduler.scheduleAtFixedRate(() -> {
            try {
                statuses[0] = request.call(); //update current status
                if (statuses[0] == 200) {
                    downLatch.countDown();
                    scheduler.shutdownNow();
                }
            } catch (Exception e) {
                //do nothing
            }
        }, 30, 10, SECONDS); //30 sec start delay, repeat every 5 sec

        downLatch.await(5, MINUTES); //wait no longer than 15 sec
        scheduler.shutdownNow(); //stop executor immediately after timeout
        assertEquals(statuses[0], 200); // throw exception if response status not 200;

        //run selenium if webapp in docker is available
        performer = new SeleniumPerformer();
        performer.openHomePage(homePage);
    }

    @AfterAll
    public static void test9_downDocker() throws IOException {
        performer.closeWindow();
        dockerRunner.down();
    }

    @Test
    void managerCabinetTest() {
        assertNotNull(performer.findElementByLink("SIGN IN"));

        //go to sign in page
        performer.clickElement(performer.findElementByLink("SIGN IN"));
        assertEquals(performer.getTitle(), "Sign in");

        //input in sign in wrong values
        performer.fieldForm("login", "badGuy");
        performer.fieldForm("password", "test");
        performer.clickElement(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Sign in");
        assertNotNull(performer.findElementById("error"));

        //input in sign with right values
        performer.fieldForm("login", "stas");
        performer.fieldForm("password", "test");
        performer.clickElement(performer.findElementByName("submit"));
        assertEquals(performer.getTitle(), "Multiverse mobile");
        assertNotNull(performer.findElementByLink("MANAGEMENT"));
        assertNotNull(performer.findElementByLink("LOG OUT"));
        //go to manage cabinet->options
        performer.clickElement(performer.findElementByLink("MANAGEMENT"));
        assertEquals(performer.getTitle(), "Manager cabinet");

        //test options
        performer.clickElement(performer.findElementByLink("Options"));
        assertEquals(performer.getTitle(), "Options");
        performer.clickElement(performer.findElementByLink("2"));
        performer.clickElement(performer.findElementByLink("Next"));

        //see option details
        performer.clickElement(performer.findElementByClass("btn-info"));
        assertEquals(performer.getTitle(), "Option");

        //edit option
        performer.clickElement(performer.findElementByClass("btn-warning"));
        assertEquals(performer.getTitle(), "Create option");

        //back to options
        performer.clickElement(performer.findElementByLink("Back to options"));
        // Thread.sleep(3000);
        assertEquals(performer.getTitle(), "Options");

        //create new
        performer.clickElement(performer.findElementByLink("Create new option"));
        assertEquals(performer.getTitle(), "Create option");

        //try to create with errors
        performer.fieldForm("price", "-5");
        performer.scrollToElement(performer.findElementByClass("btn-success"));
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Create option");
        assertNotNull(performer.findElementById("name.errors"));
        assertNotNull(performer.findElementById("price.errors"));

        //fill with values

        performer.findElementById("price").clear();
        performer.findElementById("cost").clear();
        performer.fieldForm("name", "Test option");
        performer.fieldForm("price", "99.99");
        performer.fieldForm("cost", "10");
        performer.fieldForm("desc", "This is test option for presentation");
        performer.selectAll("bootstrap-duallistbox-nonselected-list_mandatory"); //select all mandatory
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertNotNull(performer.findElementByClass("bg-danger")); //error message
        performer.selectAll("bootstrap-duallistbox-selected-list_mandatory");
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_mandatory", "Hologram");
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_mandatory", "Local network");
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Option");//success create

        //find client by wrong number
        performer.clickElement(performer.findElementByLink("Clients"));
        assertEquals(performer.getTitle(), "Clients");
        performer.fieldForm("phoneNumber", "9876543210");
        performer.submitById("find");
        performer.waitForAjax();
        assertEquals(performer.findElementById("message").getText(), "there is no such client");
        assertEquals(performer.findElementById("result").getText(), ""); //result is empty

        //find client by wrong number format
        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "999");
        performer.submitById("find");
        performer.waitForAjax();
        assertEquals(performer.findElementById("message").getText(), "must be 10 digits");

        //find client by existing number
        performer.findElementById("phoneNumber").clear();
        performer.fieldForm("phoneNumber", "9990000002");
        performer.submitById("find");
        performer.waitForAjax();
        List<WebElement> list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        performer.fieldForm("passport", "2222222222");
        performer.submitById("find1");
        performer.waitForAjax();
        assertNotEquals(performer.findElementById("result").getText(), "");
        list = performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr");
        assertEquals(list.size(), 1);

        //show client details
        performer.clickElement(performer.findElementsByPath("//*[@id=\"result\"]/table/tbody/tr/td[7]/form/input[2]").get(0)); //pick from result

        //click create contract
        performer.clickElement(performer.findElementsByPath("/html/body/div/form[2]/input[2]").get(0));
        assertEquals(performer.getTitle(), "Edit contract");
        performer.selectAll("bootstrap-duallistbox-nonselected-list_optionsIds"); //select all options
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertNotNull(performer.findElementByClass("bg-danger")); //error message
        performer.selectAll("bootstrap-duallistbox-selected-list_optionsIds"); //deselect all
        performer.selectVisible("bootstrap-duallistbox-nonselected-list_optionsIds", "Hologram");
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Contract"); //9990000007

        //log out
        performer.clickElement(performer.findElementByLink("LOG OUT"));
        assertEquals(performer.getTitle(), "Multiverse mobile");
    }

    @Test
    void userCabinetTest() {
        assertDoesNotThrow(() -> performer.findElementByLink("SIGN UP"));
        //go to sign up page
        performer.clickElement(performer.findElementByLink("SIGN UP"));
        assertEquals(performer.getTitle(), "Sign up");

        //sign up wrong values
        performer.fieldForm("login", "9876543210");
        performer.fieldForm("password", "test");
        performer.clickElement(performer.findElementByClass("btn-success"));
        assertEquals(performer.getTitle(), "Sign up");
        assertDoesNotThrow(() -> performer.findElementById("error"));

        //sign up with right values
        performer.findElementById("login").clear();
        performer.findElementById("password").clear();
        performer.fieldForm("login", "9990000007");
        performer.fieldForm("password", "test");
        performer.clickElement(performer.findElementByClass("btn-success"));

        assertEquals(performer.getTitle(), "Sign in");
        performer.fieldForm("login", "9990000007");
        performer.fieldForm("password", "test");
        performer.clickElement(performer.findElementByClass("btn-success"));

        assertDoesNotThrow(() -> performer.findElementByLink("Cabinet"));
        assertDoesNotThrow(() -> performer.findElementByLink("LOG OUT"));

        //go to manage cabinet->options
        performer.clickElement(performer.findElementByLink("Cabinet"));
        assertEquals(performer.getTitle(), "User cabinet");
        assertEquals(performer.findElementsByPath("//*[@id=\"tariffOption7\"]/div[2]").get(0).getText(), "Can't be deactivated");
        assertEquals(performer.findElementByClass("btn-danger").getAttribute("value"), "Delete");

        //check tariffs
        performer.clickElement(performer.findElementByLink("Tariffs"));
        List<WebElement> tariffs = performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]/div");
        assertEquals(tariffs.size(), 3);
        performer.clickElement(performer.findElementById("moreTariffs"));
        performer.waitForAjax();
        tariffs = performer.findElementsByPath("//*[@id=\"tariffs\"]/div[1]/div");
        assertEquals(tariffs.size(), 4);

        //check available options
        performer.clickElement(performer.findElementByLink("Available options"));
        List<WebElement> options = performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]/div");
        assertEquals(options.size(), 3);
        performer.clickElement(performer.findElementById("moreOptions"));
        performer.waitForAjax();
        tariffs = performer.findElementsByPath("//*[@id=\"available-options\"]/div[1]/div");
        assertEquals(tariffs.size(), 6);

        //block number
        performer.clickElement(performer.findElementByLink("Block number"));
        assertDoesNotThrow(() -> performer.findElementByClass("badge-danger")); //block badge
        assertTrue(performer.findElementById("tab1").getAttribute("class").contains("disabled"));

        //unblock number
        performer.clickElement(performer.findElementByLink("Unblock number"));
        assertFalse(performer.findElementById("tab1").getAttribute("class").contains("disabled"));


        //buy options
        performer.clickElement(performer.findElementByLink("Available options"));
        performer.clickElement(performer.findElementsByPath("//*[@id=\"newOption3\"]/div[2]/button").get(0));
        performer.waitForAjax();
        assertDoesNotThrow(() -> performer.findElementById("optionInCart3"));
        performer.clickElement(performer.findElementById("buy-button"));
        performer.waitForAjax();

        //close modal
        performer.clickElement(performer.findElementByClass("close"));
        performer.waitForModalClose("cart-message");

        //buy another
        performer.clickElement(performer.findElementById("moreOptions"));
        performer.waitForAjax();
        performer.clickElement(performer.findElementsByPath("//*[@id=\"newOption4\"]/div[2]/button").get(0));
        performer.waitForAjax();
        assertDoesNotThrow(() -> performer.findElementById("optionInCart4"));
        performer.clickElement(performer.findElementById("buy-button"));
        performer.waitForAjax();
        assertEquals(performer.findElementsByPath("//*[@id=\"active-options\"]/div/div").size(), 4);

        //delete option
        performer.clickElement(performer.findElementsByPath("//*[@id=\"option4\"]/div[2]/form/input[2]").get(0));
        performer.waitForAjax();

        //close modal
        performer.clickElement(performer.findElementByClass("close"));
        performer.waitForModalClose("cart-message");

        //delete another
        performer.clickElement(performer.findElementsByPath("//*[@id=\"option3\"]/div[2]/form/input[2]").get(0));
        performer.waitForAjax();
        performer.clickElement(performer.findElementsByPath("//*[@id=\"option4\"]/div[2]/form/input[2]").get(0));
        performer.waitForAjax();
        assertEquals(performer.findElementsByPath("//*[@id=\"active-options\"]/div/div").size(), 2);

        //add and delete from cart
        performer.clickElement(performer.findElementByLink("Available options"));
        performer.clickElement(performer.findElementsByPath("//*[@id=\"newOption2\"]/div[2]/button").get(0));
        assertDoesNotThrow(() -> performer.findElementById("optionInCart2"));
        assertTrue(performer.findElementById("cart-sum").getText().contains("Total: $300"));
        performer.refresh();
        assertDoesNotThrow(() -> performer.findElementById("optionInCart2"));
        performer.clickElement(performer.findElementsByPath("//*[@id=\"optionInCart2\"]/button").get(0));
        performer.waitForAjax();
        assertEquals(performer.findElementsByPath("//*[@id=\"cart-body\"]/p").size(), 0);
        assertEquals(performer.findElementById("cart-sum").getText(), "Total: $0");

        //log out
        performer.clickElement(performer.findElementByLink("LOG OUT"));
        assertEquals(performer.getTitle(), "Multiverse mobile");
    }

}
