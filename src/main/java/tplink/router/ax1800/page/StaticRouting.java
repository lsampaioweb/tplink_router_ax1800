package tplink.router.ax1800.page;

import java.net.URISyntaxException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import tplink.router.ax1800.model.RoutingRule;

public class StaticRouting extends BasePage {

	private static final String URL = "#routingAdv";
	private static final String XPATH_BUTTON_ADD = "//a[@tbar-name='add']";

	public StaticRouting() {
		this(URL);
	}

	public StaticRouting(String url) {
		super(url);
	}

	public void addStaticRoutings(WebDriver driver, WebDriverWait wait, List<RoutingRule> routingRules)
			throws URISyntaxException {
		goTo(driver, wait);

		waitUntilPresenceOfElement(wait, By.xpath(XPATH_BUTTON_ADD));
	}

}
