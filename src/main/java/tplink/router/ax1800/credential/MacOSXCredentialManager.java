package tplink.router.ax1800.credential;

public class MacOSXCredentialManager extends BaseCredentialManager {

	@Override
	public String getPassword(String passwordID) {
		String command = String.format("security find-generic-password -a $USER -s '%s' -w", passwordID);

		String[] commands = { "/bin/zsh", "-c", command };

		return getPassword(commands);
	}

}
