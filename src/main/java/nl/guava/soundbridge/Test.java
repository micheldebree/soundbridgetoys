package nl.guava.soundbridge;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {
				"soundbridge.xml", "rss.xml" });

		BeanFactory factory = (BeanFactory) appContext;

		Soundbridge sb = (Soundbridge) factory.getBean("soundbridge");

		try {
			System.out.println("Volume: " + sb.getVolume());
		} catch (ConnectionException e) {
			e.printStackTrace();
			return;
		} catch (CommandException e) {
			e.printStackTrace();
		}

		try {
			List<String> servers = sb.getVisualizers();
			for (String server : servers) {
				System.out.println(server);
			}
		} catch (CommandException e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}

	}

}
