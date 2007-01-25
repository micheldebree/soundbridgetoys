package nl.guava.soundbridge;

public class Soundbridge {

	private String host;
	
	// this is the connection to the simple RCP protocol
	private Connection simpleConnection;
	private final static int SIMPLE_PORT = 4444;
	
	private SketchMode sketchMode;
	
	public void connect() throws ConnectionException {
		if (host == null) {
			throw new ConnectionException("No host configured.");
		}
		simpleConnection = new Connection(host, SIMPLE_PORT);
		simpleConnection.open();
	}
	
	public SketchMode getSketchMode() throws ConnectionException {
		if (sketchMode == null) {
			sketchMode = new SketchMode(getSimpleConnection());
		}
		return sketchMode;
	}

	private Connection getSimpleConnection() throws ConnectionException {
		if (simpleConnection == null) {
			throw new ConnectionException("No connection configured.");
		}
		if (!simpleConnection.isReady()) {
			throw new ConnectionException("Connection not open.");
		}
		return simpleConnection;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	} 
	
	
	
}
