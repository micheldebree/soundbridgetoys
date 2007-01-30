package nl.guava.soundbridge;

import java.util.List;

/**
 * The soundbridge itself :)
 * @author michel
 */
public class Soundbridge {

	private String host;

	private SimpleConnection simpleConnection;
	private AdvancedConnection advancedConnection;

	private AdvancedConnection getSession() throws ConnectionException {

		if (advancedConnection == null) {
			advancedConnection = new AdvancedConnection(host);
			advancedConnection.open();
		}
		return advancedConnection;
	}

	public SimpleConnection getSimpleConnection() throws ConnectionException {
		if (simpleConnection == null) {
			simpleConnection = new SimpleConnection(host);
			simpleConnection.open();
		}
		return simpleConnection;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getVolume() throws CommandException, ConnectionException {
		String result = Command.GETVOLUME.execute(getSession());
		return Integer.parseInt(result);
	}

	public List<String> getServers() throws CommandException, ConnectionException {
		return Command.LISTSERVERS.executeList(getSession());
	}
	
	public List<String> getVisualizers() throws CommandException, ConnectionException {
		return Command.LISTVISUALIZERS.executeList(getSession());
	}

}
