package app.core.Login;

public class LoginItem {

	private String token;
	private String clientName;

	public LoginItem() {

	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
