package in.inagaki.xmppjetty;

import in.inagaki.xmppjetty.bean.Data;
import in.inagaki.xmppjetty.bean.Message;
import in.inagaki.xmppjetty.bean.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.websocket.WebSocket;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class XmppWebSocket implements WebSocket.OnTextMessage, RosterListener,
		MessageListener, ChatManagerListener {

	protected Connection connection;
	protected XMPPConnection talk;

	@Override
	public void onOpen(Connection arg0) {
		this.connection = arg0;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		talk.disconnect();
	}

	@Override
	public void onMessage(String arg0) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Message recMsg = mapper.readValue(arg0, Message.class);
			if (recMsg.getType().equals("login")) {

				// Set XMPP connection
				SmackConfiguration.setPacketReplyTimeout(5000);
				ConnectionConfiguration config = new ConnectionConfiguration(
						recMsg.getData().getServer(), 5222, "gmail.com");
				config.setSASLAuthenticationEnabled(false);

				// Log in to XMPP server
				talk = new XMPPConnection(config);
				talk.connect();
				talk.login(recMsg.getData().getUserName(), recMsg.getData()
						.getPassword());

				Message sndMsg = new Message("login");
				connection.sendMessage(mapper.writeValueAsString(sndMsg));

				Roster roster = talk.getRoster();
				roster.addRosterListener(this);
				talk.getChatManager().addChatListener(this);

				Collection<RosterEntry> entries = roster.getEntries();
				List<User> users = new ArrayList<User>();

				for (RosterEntry entry : entries) {
					System.out.println(entry);
					Presence presence = roster.getPresence(entry.getUser());
					String mode = null;
					if (presence.isAvailable()) {
						mode = "available";
					}
					if (presence.isAway()) {
						mode = "away";
					}
					String name = entry.getName() != null ? entry.getName() : entry.getUser();
					users.add(new User(name, entry.getUser(), mode));
				}

				Message sndRoster = new Message("roster", users);

				System.out.println(mapper.writeValueAsString(sndRoster));
				connection.sendMessage(mapper.writeValueAsString(sndRoster));

			} else if (recMsg.getType().equals("chat")) {
				ChatManager cm = talk.getChatManager();
				Chat chat = cm.createChat(recMsg.getData().getRemote(), this);
				chat.sendMessage(recMsg.getData().getText());
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void chatCreated(Chat chat, boolean createdLocally) {
		System.out.println(">>chatCreated:" + createdLocally);
		if (!createdLocally) {
			System.out.println(chat.getParticipant());
			chat.addMessageListener(this);
		}

	}

	@Override
	public void processMessage(Chat chat,
			org.jivesoftware.smack.packet.Message messgage) {
		if (messgage.getBody() == null) {
			return;
		}
		StringTokenizer st = new StringTokenizer(messgage.getFrom(), "/");
		Message sndMsg = new Message("chat", new Data(st.nextToken(), messgage.getBody()));

		ObjectMapper mapper = new ObjectMapper();
		try {
			connection.sendMessage(mapper.writeValueAsString(sndMsg));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		System.out.println(">>entriesAdded:");
		Iterator<String> it = arg0.iterator();
		while (it.hasNext()) {
			String entry = it.next();
			System.out.println(entry);
		}
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		System.out.println(">>entriesDeleted:");
		Iterator<String> it = arg0.iterator();
		while (it.hasNext()) {
			String entry = it.next();
			System.out.println(entry);
		}
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		System.out.println(">>entriesUpdated:");
		Iterator<String> it = arg0.iterator();
		while (it.hasNext()) {
			String entry = it.next();
			System.out.println(entry);
		}
	}

	@Override
	public void presenceChanged(Presence presence) {
		System.out.println(">>presenceChanged:" + presence.getFrom() + " " + presence);
		String mode = null;
		if (presence.isAvailable()) {
			mode = "available";
		}
		if (presence.isAway()) {
			mode = "away";
		}
		StringTokenizer st = new StringTokenizer(presence.getFrom(), "/");
		User user = new User(null, st.nextToken(), mode);
		List<User> users = new ArrayList<User>();
		users.add(user);
		Message sndMsg = new Message("presence", users);
		ObjectMapper mapper = new ObjectMapper();
		try {
			connection.sendMessage(mapper.writeValueAsString(sndMsg));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
