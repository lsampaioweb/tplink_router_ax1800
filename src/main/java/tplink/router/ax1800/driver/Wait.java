package tplink.router.ax1800.driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait {

	private static final int WAIT_SECONDS = 5;
	private int waitSeconds;

	public Wait() {
		this(WAIT_SECONDS);
	}

	public Wait(int waitSeconds) {
		setWaitSeconds(waitSeconds);
	}

	public WebDriverWait getDriver(WebDriver driver) {
		return new WebDriverWait(driver, getWaitDurationSeconds());
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

}
