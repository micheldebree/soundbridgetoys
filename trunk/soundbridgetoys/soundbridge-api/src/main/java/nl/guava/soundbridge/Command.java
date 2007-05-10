package nl.guava.soundbridge;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A command to be used over an advanced connection.
 * 
 * @author michel
 * 
 */
public enum Command {

	GETVOLUME("GetVolume"), SETVOLUME("SetVolume"), LISTSERVERS("ListServers"), LISTVISUALIZERS("ListVisualizers");

	private final static Logger LOG = Logger.getLogger(Command.class);

	private final static String ERR_UNKNOWNCOMMAND = "UnknownCommand";

	private final static String RES_LISTSIZE = "ListResultSize";

	private final static String RES_LISTEND = "ListResultEnd";

	private final String id;

	Command(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	/**
	 * Execute a single-result command.
	 * 
	 * @param connection
	 *            The connection to send the command to.
	 * @return The result of the command.
	 * @throws CommandException
	 */
	public String execute(AdvancedConnection connection) throws CommandException {

		connection.writeLine(getId());
		String result;
		try {
			result = connection.readLine();
		} catch (ConnectionException e) {
			throw new CommandException("Failed to read result.", e);
		}

		String response = parseResponse(result);
		LOG.debug("Response: '" + response + "'");

		if (!ERR_UNKNOWNCOMMAND.equals(response)) {
			return response;
		} else {
			throw new CommandException("Unknown command: " + getId());
		}
	}

	/**
	 * Execute a synchronous multi-result command.
	 * 
	 * @param connection
	 *            The connection to send the command to.
	 * @return A list of results.
	 * @throws CommandException
	 */
	public List<String> executeList(AdvancedConnection connection) throws CommandException {

		connection.writeLine(getId());
		try {
			return parseListResponse(connection);
		} catch (ConnectionException e) {
			throw new CommandException("Command failed.", e);
		}
	}

	private List<String> parseListResponse(Connection connection) throws ConnectionException, CommandException {
		String response = parseResponse(connection.readLine());
		
		// get list size
		int listSize = 0;
		if (response.startsWith(RES_LISTSIZE)) {
			String listSizeStr = response.substring(RES_LISTSIZE.length() + 1);
			listSize = Integer.parseInt(listSizeStr);
		} else {
			throw new CommandException("Expected " + RES_LISTSIZE + ", got " + response);
		}

		// get list entries
		List<String> resultList = new ArrayList<String>();
		for (int i = 0; i < listSize; i++) {
			resultList.add(parseResponse(connection.readLine()));
		}

		// check end marker
		response = parseResponse(connection.readLine());
		if (!response.startsWith(RES_LISTEND)) {
			throw new CommandException("Expected " + RES_LISTEND + ", got " + response);
		}

		return resultList;

	}

	private String parseResponse(String response) throws CommandException {

		final String commandPrefix = id + ": ";

		if (response.startsWith(commandPrefix)) {
			return response.substring(commandPrefix.length());
		} else {
			throw new CommandException("Unknown response: " + response);
		}
	}

}
