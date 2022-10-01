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

  protected static ResourceBundle getI18n() {
    if (null == i18n) {
      i18n = ResourceBundle.getBundle("Messages");
    }

    return i18n;
  }

  public static BaseCredentialManager getCredentialManager() throws Exception {
    String os = getOperatingSystem();

    if (isMac(os)) {
      return new MacOSXCredentialManager();
    } else if (isUnix(os)) {
      return new LinuxCredentialManager();
    } else {
      String errorMessage = String.format(getI18n().getString("operating_system_not_supported"), os);
      throw new Exception(errorMessage);
    }
  }

  private static String getOperatingSystem() {
    return System.getProperty("os.name").toLowerCase();
  }

  private static boolean isMac(String os) {
    return (os.indexOf("mac") >= 0);
  }

  private static boolean isUnix(String os) {
    return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
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