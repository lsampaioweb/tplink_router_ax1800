package tplink.router.ax1800.page;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

  private static Logger logger;
  private static ResourceBundle i18n;

  private static final String URL = "";
  private String url;

  public BasePage() {
    this(URL);
  }

  public BasePage(String url) {
    setUrl(url);
  }

  protected String getUrl() {
    return url;
  }

  private void setUrl(String url) {
    this.url = url;
  }

  protected Logger getLogger() {
    if (null == logger) {
      logger = LoggerFactory.getLogger(this.getClass());
    }

    return logger;
  }

  protected ResourceBundle getI18n() {
    if (null == i18n) {
      i18n = ResourceBundle.getBundle("Messages");
    }

    return i18n;
  }

  protected void waitUntilPageIsFullyLoaded(WebDriverWait wait) {
    wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
  }

  protected void waitUntilUrlContains(WebDriverWait wait, String fraction) {
    wait.until(ExpectedConditions.urlContains(fraction));
  }

  protected void waitUntilPresenceOfElement(WebDriverWait wait, By locator) {
    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
  }

  protected WebElement findElementByPath(WebDriverWait wait, String xpath) {
    getLogger().info(getI18n().getString("searching_xpath"), xpath);

    return wait.until(driver -> driver.findElement(By.xpath(xpath)));
  }

  protected List<WebElement> findElementsByPath(WebDriverWait wait, String xpath) {
    getLogger().info(getI18n().getString("searching_xpath"), xpath);

    List<WebElement> webElements = wait.until(driver -> driver.findElements(By.xpath(xpath)));

    return (null != webElements) ? webElements : new ArrayList<>();
  }

  protected void setInputValue(WebElement webElement, String value) {
    if (null != webElement) {
      webElement.clear();
      webElement.sendKeys(value);
      webElement.sendKeys(Keys.TAB);

      return;
    }

    String errorMessage = String.format(getI18n().getString("null_webelement_cannot_receive_input"), value);
    throw new IllegalArgumentException(errorMessage);
  }

  protected void elementClick(WebElement webElement) {
    if (null != webElement) {
      webElement.click();

      return;
    }

    String errorMessage = getI18n().getString("webelement_cannot_be_null");
    throw new IllegalArgumentException(errorMessage);
  }

  public void goTo(WebDriver driver, WebDriverWait wait) throws URISyntaxException {
    URI uri = new URI(driver.getCurrentUrl()).resolve(getUrl());

    goTo(driver, wait, uri.toString());
  }

  public void goTo(WebDriver driver, WebDriverWait wait, String url) throws URISyntaxException {
    driver.navigate().to(url);

    waitUntilPageIsFullyLoaded(wait);

    getLogger().info(getI18n().getString("current_url"), driver.getCurrentUrl());
  }

}
