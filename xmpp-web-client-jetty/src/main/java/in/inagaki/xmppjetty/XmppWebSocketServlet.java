package in.inagaki.xmppjetty;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class XmppWebSocketServlet extends WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6096357638151030175L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
		// TODO Auto-generated method stub
		return new XmppWebSocket();
	}

}
