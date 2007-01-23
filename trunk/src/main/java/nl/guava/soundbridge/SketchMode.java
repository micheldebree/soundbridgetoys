package nl.guava.soundbridge;

/**
 * Sketch mode in the simple soundbridge RCP protocol.
 * @author michel
 *
 */
public class SketchMode {
	
	private final static int WIDTH = 280;
	private final static int HEIGHT = 16;

	public final static String CMD_OPEN = "sketch";
	public final static String CMD_CLOSE = "quit";
	public final static String CMD_MARQUEE = "marquee";
	public final static String CMD_FONT = "font";
	
	public final static int FONT_FIXED8 = 1;
	public final static int FONT_ZURICH_BOLD16 = 2;
	public final static int FONT_ZURICH_BOLD32 = 3;
	public final static int FONT_ZURICH_BOLD16b = 10;
	public final static int FONT_ZURICH_LIGHT16 = 11;
	public final static int FONT_FIXED16 = 12;
	public final static int FONT_SANSSERIF16 = 14;
	
	
	private Connection connection;
	
	public SketchMode(Connection connection) throws ConnectionException {
		this.connection = connection;
		connection.writeCommand(CMD_OPEN);
	}
	
	public void close() throws ConnectionException {
		connection.writeCommand(CMD_CLOSE);
	}

	public void setFont(int font) throws ConnectionException {
		connection.writeCommand(CMD_FONT + ' ' + font);
	}

	// TODO: there seems to be a maximum length...?
	public void marquee(String text) throws ConnectionException {
		connection.writeCommand(CMD_MARQUEE + " \"" + text + "\"");
	}
	
}
