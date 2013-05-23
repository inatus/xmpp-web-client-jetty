package in.inagaki.xmppjetty.bean;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Message {
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Data")
	private Data data;
	
	@JsonProperty("Roster")
	private List<User> roster;
	
	public Message(String type, Data data) {
		super();
		this.type = type;
		this.data = data;
	}

	public Message(String type, List<User> roster) {
		super();
		this.type = type;
		this.roster = roster;
	}

	public Message(String type) {
		super();
		this.type = type;
	}

	public Message() {
		
	}

	public String getType() {
		return type;
	}

	public Data getData() {
		return data;
	}

}
