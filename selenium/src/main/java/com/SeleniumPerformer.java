package com;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeleniumPerformer {
    static {
        System.setProperty("webdriver.chrome.driver", "c:/Public/chromedriver/chromedriver.exe");
    }

    JavascriptExecutor js;
    private WebDriver driver;
    private String url = "http://192.168.99.100:8080/mobile";

    public SeleniumPerformer() {

        ChromeOptions options = new ChromeOptions();
        this.driver =new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
    }

    public void openWindow() {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void closeWindow() {
        this.driver.close();
    }

    public String getTitle() {
        return this.driver.getTitle();
    }


    public void clickLink(String linkText) {
        Actions builder = new Actions(driver);
        WebElement element = this.driver
                .findElement(By.linkText(linkText));
        builder.moveToElement(element)
                .build()
                .perform();
        driver.findElement(By.linkText(linkText)).click();
    }

    public void submit(WebElement element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(element)
                .build()
                .perform();
        element.submit();

    }

    public WebElement findElementByLink(String linkText) {
        return driver.findElement(By.linkText(linkText));
    }

    public WebElement findElementById(String id) {
        return driver.findElement(By.id(id));
    }

    public WebElement findElementByClass(String cl) {
        return driver.findElement(By.className(cl));
    }

    public WebElement findElementByName(String name) {
        return driver.findElement(By.name(name));
    }

    public List<WebElement> findElementsByPath(String path) {
        return driver.findElements(By.xpath(path));
    }

    public void selectAll(String id) {
        Select select = new Select(findElementById(id));
        List<WebElement> options = select.getOptions();

        for (int i = options.size() - 1; i >= 0; i--) {
            select.selectByIndex(i);
        }
    }

    public void selectVisible(String id, String name) {
        Select select = new Select(findElementById(id));
        select.selectByVisibleText(name);
    }

    public void fieldForm(String fieldId, String text) {
        WebElement element = driver.findElement(By.id(fieldId));
        element.sendKeys(text);
    }
}
