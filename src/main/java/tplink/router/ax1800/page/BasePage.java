package tplink.router.ax1800.page;

import java.net.URI;
import java.net.URISyntaxException;
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

	private void waitUntilPageIsFullyLoaded(WebDriverWait wait) {
		wait.until(
				driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
	}

	protected WebElement findElementByPath(WebDriverWait wait, String xpath) {
		getLogger().debug(getI18n().getString("searching_xpath"), xpath);

		return wait.until(driver -> driver.findElement(By.xpath(xpath)));
	}

	protected void setInputValue(WebElement webElement, String value) {
		if (null != webElement) {
			webElement.clear();
			webElement.sendKeys(value);
			webElement.sendKeys(Keys.TAB);
			return;
		}

		String errorMessage = String.format(getI18n().getString("webelement_cannot_be_null"), value);
		throw new IllegalArgumentException(errorMessage);
	}

	protected void waitUntilUrlContains(WebDriverWait wait, String fraction) {
		wait.until(ExpectedConditions.urlContains(fraction));
	}

	protected void waitUntilPresenceOfElement(WebDriverWait wait, By locator) {
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public void goTo(WebDriver driver, WebDriverWait wait) throws URISyntaxException {
		goTo(driver, wait, new URI(driver.getCurrentUrl()).resolve(getUrl()).toString());
	}

	public void goTo(WebDriver driver, WebDriverWait wait, String url) throws URISyntaxException {
		driver.navigate().to(url);

		waitUntilPageIsFullyLoaded(wait);

		getLogger().info(getI18n().getString("current_url"), driver.getCurrentUrl());
	}

}