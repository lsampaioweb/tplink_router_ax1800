package tplink.router.ax1800;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tplink.router.ax1800.driver.Chrome;
import tplink.router.ax1800.driver.Wait;
import tplink.router.ax1800.model.RoutingRule;
import tplink.router.ax1800.page.Login;
import tplink.router.ax1800.page.StaticRouting;

public class Setup {

	private static Logger logger;
	private ResourceBundle i18n;

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

//		String netmask = "255.255.255.240";
//		String gateway = "192.168.0.3";
//		String interfaceValue = "LAN";

//		routingRules.add(new RoutingRule("10.0.1.0", netmask, gateway, interfaceValue, "Network_Mgnt"));
//		routingRules.add(new RoutingRule("10.0.3.0", netmask, gateway, interfaceValue, "Server_Mgnt"));
//		routingRules.add(new RoutingRule("10.0.4.0", netmask, gateway, interfaceValue, "Server_Cluster_Mgnt"));
//		routingRules.add(new RoutingRule("10.0.100.0", netmask, gateway, interfaceValue, "JumpServers"));
		return routingRules;
	}

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

}
