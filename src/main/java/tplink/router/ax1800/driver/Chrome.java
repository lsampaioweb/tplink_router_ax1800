package tplink.router.ax1800.driver;

import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Chrome {

	private static final int WAIT_SECONDS = 5;
	private static final boolean HEADLESS = false;
	private static final String[] ARGUMENTS = { "ignore-certificate-errors" };
	private int waitSeconds;
	private boolean headless;
	private String[] arguments;

	public Chrome() {
		this(WAIT_SECONDS, HEADLESS, ARGUMENTS);
	}

	public Chrome(int waitSeconds, boolean headless, String[] arguments) {
		setWaitSeconds(waitSeconds);
		setHeadless(headless);
		setArguments(arguments);
	}

	public ChromeDriver getDriver() {
		initialize();

		ChromeDriver driver = new ChromeDriver(getDefaultOptions());

		driver.manage().timeouts().implicitlyWait(getWaitDurationSeconds());
		driver.manage().window().maximize();

		return driver;
	}

	private void initialize() {
		WebDriverManager.chromedriver().setup();
	}

	private ChromeOptions getDefaultOptions() {
		ChromeOptions options = new ChromeOptions();

		options.setHeadless(isHeadless());
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

	private boolean isHeadless() {
		return headless;
	}

	private void setHeadless(boolean headless) {
		this.headless = headless;
	}

	private String[] getArguments() {
		return arguments;
	}

	private void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

}
