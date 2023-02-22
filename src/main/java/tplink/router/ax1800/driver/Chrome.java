package tplink.router.ax1800.driver;

import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Chrome {

	private static final int WAIT_SECONDS = 5;
	private static final String[] ARGUMENTS = { "--ignore-certificate-errors", "--no-sandbox", "--headless", "--window-size=1920x1080", "--start-maximized" };
	private int waitSeconds;
	private String[] arguments;

	public Chrome() {
		this(WAIT_SECONDS, ARGUMENTS);
	}

	public Chrome(int waitSeconds, String[] arguments) {
		setWaitSeconds(waitSeconds);
		setArguments(arguments);
	}

	public ChromeDriver getDriver() {
		initialize();

		ChromeDriver driver = new ChromeDriver(getDefaultOptions());

		driver.manage().timeouts().implicitlyWait(getWaitDurationSeconds());

		return driver;
	}

	private void initialize() {
		WebDriverManager.chromedriver().setup();
	}

	private ChromeOptions getDefaultOptions() {
		ChromeOptions options = new ChromeOptions();

		options.addArguments(getArguments());

		return options;
	}

	private Duration getWaitDurationSeconds() {
		return Duration.ofSeconds(getWaitSeconds());
	}

	private int getWaitSeconds() {
		return waitSeconds;
	}

	private void setWaitSeconds(int waitSeconds) {
		this.waitSeconds = waitSeconds;
	}

	private String[] getArguments() {
		return arguments;
	}

	private void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

}
