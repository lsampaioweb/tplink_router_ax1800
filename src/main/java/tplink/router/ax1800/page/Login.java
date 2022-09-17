package tplink.router.ax1800.page;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends BasePage {

	private static final Logger logger = LoggerFactory.getLogger(Login.class);

	private static final String URL = "https://tplinkwifi.net/";

	private static final String XPATH_ELEMENT_PASSWORD = "//input[contains (@type, 'password')]";
	private static final String URL_NETWORK_MAP = "#networkMap";
	private static final String XPATH_INTERNET_STATUS = "//span[text()='Internet Status']";

	public Login() {
		this(URL);
	}

	public Login(String url) {
		super(url);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void doLogin(WebDriver driver, WebDriverWait wait) {
		goTo(driver, wait);

		WebElement webElement = getPasswordInputElement(wait);

		String password = getPasswordFromSecretManager();

		setInputValue(webElement, password);
		webElement.sendKeys(Keys.ENTER);

		waitUntilUrlContains(wait, URL_NETWORK_MAP);
		waitUntilPresenceOfElement(wait, By.xpath(XPATH_INTERNET_STATUS));
	}

	private String getPasswordFromSecretManager() {
		try {
			String[] commands = { "/bin/zsh", "-c", "security find-generic-password -a $USER -s 'TP-Link-AX20' -w" };
			ProcessBuilder builder = new ProcessBuilder(commands);
			builder.redirectErrorStream(true);

			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String password = reader.readLine();

			int exitCode = process.waitFor();
			if (0 != exitCode) {
				String errorMessage = String.format(getI18n().getString("password_was_not_found"), exitCode);
				throw new Exception(errorMessage);
			}

			if (isNullOrEmpty(password)) {
				throw new Exception(getI18n().getString("password_cannot_be_null"));
			}

			return password;

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private boolean isNullOrEmpty(String value) {
		return (value == null || value.isEmpty() || value.trim().isEmpty());
	}

	private WebElement getPasswordInputElement(WebDriverWait wait) {
		return findElementByPath(wait, XPATH_ELEMENT_PASSWORD);
	}

}
