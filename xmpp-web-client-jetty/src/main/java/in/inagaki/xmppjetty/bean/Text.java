package in.inagaki.xmppjetty.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class Text {
	@JsonProperty("Remote")
	private String remote;
	
	@JsonProperty("Text")
	private String text;

	public Text(String remote, String text) {
		super();
		this.remote = remote;
		this.text = text;
	}
	
	public Text() {
		
	}
}
