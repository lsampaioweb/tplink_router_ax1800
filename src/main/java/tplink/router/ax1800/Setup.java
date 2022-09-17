package tplink.router.ax1800;

import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tplink.router.ax1800.driver.Chrome;
import tplink.router.ax1800.driver.Wait;
import tplink.router.ax1800.page.Login;

public class Setup {

	private static final Logger logger = LoggerFactory.getLogger(Setup.class);
	private ResourceBundle i18n;

	public static void main(String[] args) {
		Setup app = new Setup();
		app.run();
	}

	private void run() {
		WebDriver driver = null;

		try {
			logger.info(getI18n().getString("app_initializing"));

			driver = new Chrome().getDriver();

			WebDriverWait wait = new Wait().getDriver(driver);

			doLogin(driver, wait);

			logger.info(getI18n().getString("app_finished"));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			if (null != driver) {
				driver.quit();
			}
		}
	}

	private void doLogin(WebDriver driver, WebDriverWait wait) {
		Login loginPage = new Login();

		loginPage.doLogin(driver, wait);
	}

	private ResourceBundle getI18n() {
		if (null == i18n) {
			i18n = ResourceBundle.getBundle("Messages");
		}

		return i18n;
	}

}
