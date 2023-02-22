package tplink.router.ax1800.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Logout extends BasePage {

  private static final String URL = "";

  private static final String XPATH_ELEMENT_LOGOUT_BUTTON = "//div[@id='logout-button']//a";
  private static final String XPATH_ELEMENT_LOGOUT_CONFIRM_BUTTON = "//a[@type='button' and @title='LOG OUT']";

  public Logout() {
    this(URL);
  }

  public Logout(String url) {
    super(url);
  }

  public void doLogout(WebDriver driver, WebDriverWait wait) throws Exception {
    elementClick(getLogoutElement(wait));

    waitUntilPresenceOfElement(wait, By.xpath(XPATH_ELEMENT_LOGOUT_CONFIRM_BUTTON));

    elementClick(getConfirmLogoutElement(wait));
  }

  private WebElement getLogoutElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_LOGOUT_BUTTON);
  }
  
  private WebElement getConfirmLogoutElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_LOGOUT_CONFIRM_BUTTON);
  }

}
