package tplink.router.ax1800.credential;

public class LinuxCredentialManager extends BaseCredentialManager {

	@Override
	public String getPassword(String passwordID) {
		String command = String.format("secret-tool lookup password '%s'", passwordID);

		String[] commands = { "/bin/bash", "-c", command };

		return getPassword(commands);
	}
}
