package nl.guava.soundbridge;

public class Soundbridge {

	// this is the connection to the simple RCP protocol
	private Connection simpleConnection;
	private SketchMode sketchMode;
	
	public void connect() throws ConnectionException {
		simpleConnection.open();
	}
	
	public SketchMode getSketchMode() throws ConnectionException {
		if (sketchMode == null) {
			sketchMode = new SketchMode(getSimpleConnection());
		}
		return sketchMode;
	}

	public Connection getSimpleConnection() throws ConnectionException {
		if (simpleConnection == null) {
			throw new ConnectionException("No connection configured.");
		}
		if (!simpleConnection.isReady()) {
			simpleConnection.open();
		}
		return simpleConnection;
	}

	public void setSimpleConnection(Connection simpleConnection) {
		this.simpleConnection = simpleConnection;
	} 
	
	
	
}
