package nl.guava.soundbridge;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Hello world!
 *
 */
public class HelloWorld 
{
    public static void main( String[] args )
    {
    	ClassPathResource res = new ClassPathResource("beans.xml");
    	XmlBeanFactory factory = new XmlBeanFactory(res);
    	
    	Soundbridge sb = (Soundbridge) factory.getBean("soundbridge");
    	try {
			sb.getSketchMode().marquee("Hello World!");
			sb.getSketchMode().close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
    	
    	
    }
}
