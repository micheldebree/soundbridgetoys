package nl.guava.soundbridge;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * A socket connection to the soundbridge.
 * 
 * @author michel
 * 
 */
public class Connection {

	private final static Logger LOG = Logger.getLogger(Connection.class);

	private Socket socket;

	private Reader reader;

	private PrintWriter writer;

	private String host;

	private int port;

	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Open connection.
	 * 
	 * @throws ConnectionException
	 */
	public void open() throws ConnectionException {

		if (host == null) {
			throw new ConnectionException("No host set.");
		}

		// open socket
		LOG.info("Connecting...");
		try {
			if (socket != null) {
				close();
			}
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			throw new ConnectionException("Unknown host: " + host, e);
		} catch (IOException e) {
			throw new ConnectionException("Connect failed.", e);
		}

		// open reader
		try {
			reader = new InputStreamReader(socket.getInputStream());
		} catch (IOException e) {
			throw new ConnectionException("Cannot read from socket.", e);
		}

		// open writer
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			throw new ConnectionException("Cannot write to socket.", e);
		}

		LOG.info("Connected.");

	}

	/**
	 * Close connection.
	 * 
	 * @throws ConnectionException
	 */
	public void close() throws ConnectionException {

		writer.close();

		try {
			reader.close();
		} catch (IOException e1) {
			throw new ConnectionException("Failed to close reader.", e1);
		}

		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				throw new ConnectionException("Socket failed to close.");
			}
		}
	}

	public boolean isReady() {
		return (socket != null) && socket.isConnected();
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

	private void waitForResponse() throws ConnectionException {
		LOG.debug("Waiting for response...");
		try {
			while (!reader.ready()) {
				// TODO: timeout mechanism
			}
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		read();
	}

	/**
	 * Write a command to the socket. Also waits for a response. TODO: there is
	 * no way to tell wether the command was succesful.
	 * 
	 * @param text
	 *            The command text.
	 * @throws ConnectionException
	 */
	public void writeCommand(String text) throws ConnectionException {
		LOG.debug("Writing: " + text);
		writer.println(text);
		waitForResponse();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
