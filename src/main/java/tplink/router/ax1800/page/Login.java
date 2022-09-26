package tplink.router.ax1800.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import tplink.router.ax1800.credential.BaseCredentialManager;
import tplink.router.ax1800.credential.LinuxCredentialManager;
import tplink.router.ax1800.credential.MacOSXCredentialManager;

public class Login extends BasePage {

	private static final String URL = "https://tplinkwifi.net/";

	private static final String ROUTER_PASSWORD_ID = "TP-Link-AX20";

	private static final String XPATH_ELEMENT_PASSWORD = "//input[contains (@type, 'password')]";
	private static final String XPATH_INTERNET_STATUS = "//span[text()='Internet Status']";

	public Login() {
		this(URL);
	}

	public Login(String url) {
		super(url);
	}

	public void doLogin(WebDriver driver, WebDriverWait wait) throws Exception {
		goTo(driver, wait);

		WebElement webElement = getPasswordInputElement(wait);
		String password = getCredentialManager().getPassword(ROUTER_PASSWORD_ID);

		setInputValue(webElement, password);
		webElement.sendKeys(Keys.ENTER);

		waitUntilPresenceOfElement(wait, By.xpath(XPATH_INTERNET_STATUS));
	}

	private WebElement getPasswordInputElement(WebDriverWait wait) {
		return findElementByPath(wait, XPATH_ELEMENT_PASSWORD);
	}

	private BaseCredentialManager getCredentialManager() throws Exception {
		String os = getOperatingSystem();

		if (isMac(os)) {
			return new MacOSXCredentialManager();
		} else if (isUnix(os)) {
			return new LinuxCredentialManager();
		} else {
			String errorMessage = String.format(getI18n().getString("operating_system_not_supported"), os);
			throw new Exception(errorMessage);
		}

	}

	private String getOperatingSystem() {
		return System.getProperty("os.name").toLowerCase();
	}

	private boolean isMac(String os) {
		return (os.indexOf("mac") >= 0);
	}

	private boolean isUnix(String os) {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
	}

}
