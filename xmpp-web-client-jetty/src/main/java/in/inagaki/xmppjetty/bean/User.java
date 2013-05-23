package in.inagaki.xmppjetty.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Remote")
	private String remote;
	
	@JsonProperty("Mode")
	private String mode;
	
	public User(String name, String remote, String status) {
		super();
		this.name = name;
		this.remote = remote;
		this.mode = status;
	}

	public User() {
		
	}
	
}
