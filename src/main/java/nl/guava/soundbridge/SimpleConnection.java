package nl.guava.soundbridge;

import java.io.IOException;

/**
 * A connection to the simple RCP protocol.
 * @author michel
 *
 */
public class SimpleConnection extends Connection {

	private final static int PORT = 4444;
	
	private SketchMode sketchMode;
	
	public SimpleConnection(String host) {
		super(host, PORT);
	}
	
	public SketchMode getSketchMode() throws ConnectionException {
		if (sketchMode == null) {
			sketchMode = new SketchMode(this);
		}
		return sketchMode;
	}
	
	/**
	 * Read data from socket.
	 * 
	 * @return The text that is read.
	 * @throws ConnectionException
	 */
	private String read() throws ConnectionException {

		StringBuffer result = new StringBuffer();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while (reader.ready()) {
				result.append((char) reader.read());
			}
		} catch (IOException e) {
			throw new ConnectionException("Read failed.", e);
		}

		LOG.debug("Reading: " + result.toString());

		return result.toString();

	}

	/**
	 * Write a command to the socket. Also waits for a response. TODO: there is
	 * no way to tell wether the command was succesful.
	 * 
	 * @param text
	 *            The command text.
	 * @throws ConnectionException
	 */
	public String writeCommand(String text) throws ConnectionException {
		writeLine(text);
		return waitForResponse();
	}

	
	public String waitForResponse() throws ConnectionException {
		LOG.debug("Waiting for response...");
		try {
			while (!reader.ready()) {
				// TODO: timeout mechanism
			}
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		return read();
	}

}
