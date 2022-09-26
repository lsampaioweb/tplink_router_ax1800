package tplink.router.ax1800;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import tplink.router.ax1800.driver.Chrome;
import tplink.router.ax1800.driver.Wait;
import tplink.router.ax1800.model.RoutingRule;
import tplink.router.ax1800.page.Login;
import tplink.router.ax1800.page.StaticRouting;

public class Setup {

	private static final String STATIC_ROUTING_RULES_JSON = "static-routing-rules.json";
	private static Logger logger;
	private ResourceBundle i18n;

	private Logger getLogger() {
		if (null == logger) {
			logger = LoggerFactory.getLogger(this.getClass());
		}

		return logger;
	}

	private ResourceBundle getI18n() {
		if (null == i18n) {
			i18n = ResourceBundle.getBundle("Messages");
		}

		return i18n;
	}

	public static void main(String[] args) {
		Setup app = new Setup();
		app.run();
	}

	private void run() {
		WebDriver driver = null;

		try {
			getLogger().info(getI18n().getString("app_initializing"));

			driver = new Chrome().getDriver();

			WebDriverWait wait = new Wait().getDriver(driver);

			doLogin(driver, wait);
			addStaticRoutings(driver, wait);

			getLogger().info(getI18n().getString("app_finished"));
		} catch (Exception e) {
			getLogger().error(e.getLocalizedMessage());
		} finally {
			if (null != driver) {
				driver.quit();
			}
		}
	}

	private void doLogin(WebDriver driver, WebDriverWait wait) throws Exception {
		Login page = new Login();

		page.doLogin(driver, wait);
	}

	private void addStaticRoutings(WebDriver driver, WebDriverWait wait) throws URISyntaxException {
		StaticRouting page = new StaticRouting();

		page.addStaticRoutings(driver, wait, getRoutingRulesFromConfigFile());
	}

	private List<RoutingRule> getRoutingRulesFromConfigFile() {
		List<RoutingRule> routingRules = new ArrayList<>();

		try (InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(STATIC_ROUTING_RULES_JSON)) {

			if (null == in) {
				String errorMessage = String.format(getI18n().getString("static_routing_rules_file_not_found"),
						STATIC_ROUTING_RULES_JSON);

				throw new IllegalArgumentException(errorMessage);
			}

			final ObjectMapper mapper = new ObjectMapper();
			RoutingRule[] jsonArray = mapper.readValue(in, RoutingRule[].class);

			routingRules = Arrays.asList(jsonArray);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return routingRules;
	}

}
