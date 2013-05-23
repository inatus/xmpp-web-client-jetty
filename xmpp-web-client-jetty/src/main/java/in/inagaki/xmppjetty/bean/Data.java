package in.inagaki.xmppjetty.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class Data {
	@JsonProperty("Server")
	private String server;
	
	@JsonProperty("UserName")
	private String userName;
	
	@JsonProperty("Password")
	private String password;
	
	@JsonProperty("Remote")
	private String remote;
	
	@JsonProperty("Text")
	private String text;

	public Data(String remote, String text) {
		super();
		this.remote = remote;
		this.text = text;
	}

	public Data() {
		super();
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemote() {
		return remote;
	}

	public String getText() {
		return text;
	}
	
}
