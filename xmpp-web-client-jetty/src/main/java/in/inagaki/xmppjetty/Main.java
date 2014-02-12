package in.inagaki.xmppjetty;

import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	public static void main(String[] args) throws Exception {
		new Main();
	}

	public Main() throws Exception {

		Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream("/server.properties"));

		Server server = new Server(Integer.parseInt(prop.getProperty("port")));

		ResourceHandler rh = new ResourceHandler();
		rh.setResourceBase(this.getClass().getClassLoader().getResource("html")
				.toExternalForm());

		XmppWebSocketServlet wss = new XmppWebSocketServlet();
		ServletHolder sh = new ServletHolder(wss);
		//Preventing the WebSocket connection from terminating after 5mins of idle time
		sh.setInitParameter("maxIdleTime", "-1");
		ServletContextHandler sch = new ServletContextHandler();
		sch.addServlet(sh, "/websocket/*");

		HandlerList hl = new HandlerList();
		hl.setHandlers(new Handler[] { rh, sch });
		server.setHandler(hl);

		server.start();

		Handler handler = server.getHandler();
		if (handler instanceof WebAppContext) {
			System.out.println("found wac");
			WebAppContext rctx = (WebAppContext) handler;
			rctx.getSessionHandler().getSessionManager()
					.setMaxInactiveInterval(60);
		}

	}

}
