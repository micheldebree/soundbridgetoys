package nl.guava.soundbridge.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nl.guava.soundbridge.ConnectionException;
import nl.guava.soundbridge.SketchMode;
import nl.guava.soundbridge.Soundbridge;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * RSS Reader that displays feeds on the soundbridge.
 * @author michel
 *
 */
public class Reader {

	private final static Logger LOG = Logger.getLogger(Reader.class);

	private int font = SketchMode.FONT_ZURICH_BOLD16;

	private final static String TIMEFORMAT = "EEEE dd MMM yyyy HH:mm";

	private Soundbridge soundBridge;

	private List<Feed> feeds;

	public Soundbridge getSoundBridge() {
		return soundBridge;
	}

	public void setSoundBridge(Soundbridge soundBridge) {
		this.soundBridge = soundBridge;
	}

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.err.println("USAGE: Reader <hostname|ip address>");
			return;
		}
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"soundbridge.xml", "rss.xml"});
    	
		BeanFactory factory = (BeanFactory) appContext;
		
    	Reader reader = (Reader) factory.getBean("rssreader");
    	
		try {
			reader.getSoundBridge().setHost(args[0]);
			reader.getSoundBridge().connect();
			reader.start();
		} catch (ConnectionException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Start showing all feeds.
	 * @throws ConnectionException
	 */
	public void start() throws ConnectionException {
		for (Feed feed : getFeeds()) {
			try {
				showFeed(feed);
			} catch (SyndicationException e) {
				LOG.error("Feed syndication failed.", e);
			}
		}
	}

	/**
	 * Show one feed.
	 * @param feed The feed to show.
	 * @throws SyndicationException
	 * @throws ConnectionException
	 */
	private void showFeed(Feed feed) throws SyndicationException,
			ConnectionException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed rssFeed;
		try {
			rssFeed = input.build(new XmlReader(new URL(feed.getUrl())));
		} catch (IllegalArgumentException e) {
			throw new SyndicationException(e);
		} catch (MalformedURLException e) {
			throw new SyndicationException(e);
		} catch (FeedException e) {
			throw new SyndicationException(e);
		} catch (IOException e) {
			throw new SyndicationException(e);
		}

		List<SyndEntryImpl> entries = rssFeed.getEntries();

		
		SketchMode sketch = soundBridge.getSketchMode();

		sketch.setFont(font);

		SimpleDateFormat dateFormat = new SimpleDateFormat(TIMEFORMAT);

		
		for (SyndEntryImpl entry : entries) {

			Date entryDate = entry.getPublishedDate();
			
			StringBuilder text = new StringBuilder();	
			if (entryDate != null) {
				text.append('[' + dateFormat.format(entryDate) + "] ");
			}
			text.append(entry.getTitle());

			sketch.marquee(text.toString());
			sketch.marquee(entry.getDescription().getValue());

		}

	}

	public List<Feed> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}

	public int getFont() {
		return font;
	}

	public void setFont(int font) {
		this.font = font;
	}

}
