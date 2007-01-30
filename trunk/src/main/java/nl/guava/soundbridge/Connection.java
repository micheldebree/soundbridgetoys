package nl.guava.soundbridge;

import java.io.BufferedReader;
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
public abstract class Connection {

	protected final static Logger LOG = Logger.getLogger(Connection.class);

	private Socket socket;

	protected Reader reader;

	private BufferedReader bufferedReader;

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
			bufferedReader = new BufferedReader(reader);
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

	public void writeLine(String text) {
		LOG.debug("Writing: " + text);
		writer.println(text);
	}

	public String readLine() throws ConnectionException {
		try {
			String result = bufferedReader.readLine();
			LOG.debug("Reading: " + result);
			return result;
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
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
