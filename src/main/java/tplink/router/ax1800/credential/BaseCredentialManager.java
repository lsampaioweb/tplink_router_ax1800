package tplink.router.ax1800.credential;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCredentialManager {

	private static Logger logger;
	private static ResourceBundle i18n;

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

	public abstract String getPassword(String passwordID);

	protected String getPassword(String[] commands) {
		try {
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

			return password;

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}