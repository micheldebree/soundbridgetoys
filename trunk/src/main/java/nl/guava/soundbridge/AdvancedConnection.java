package nl.guava.soundbridge;

import org.apache.log4j.Logger;

/**
 * Connection for the advanced RCP protocol.
 * 
 * @author michel
 */
public class AdvancedConnection extends Connection {

	private final static Logger LOG = Logger.getLogger(AdvancedConnection.class);
	private final static int PORT = 5555;
	private final static String MSG_READY = "roku: ready";

	public AdvancedConnection(String host) {
		super(host, PORT);
	}

	public void open() throws ConnectionException {

		super.open();

		if (!MSG_READY.equals(readLine())) {
			throw new ConnectionException("Soundbridge not ready.");
		} else {
			LOG.info("Soundbridge ready.");
		}

	}

}
