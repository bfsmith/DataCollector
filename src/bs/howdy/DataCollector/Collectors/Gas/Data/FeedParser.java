package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FeedParser {

	protected DocumentBuilderFactory xmlFactory;
	private final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public FeedParser() {
		super();
	}

	protected String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
	
		return textVal;
	}

	protected int getIntValue(Element ele, String tagName) {
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	protected float getFloatValue(Element ele, String tagName) {
		return Float.parseFloat(getTextValue(ele,tagName));
	}

	protected DateTime getDateValue(Element ele, String tagName)
			throws ParseException {
				Date d = XML_DATE_FORMAT.parse(getTextValue(ele,tagName));
				return new DateTime(d.getTime());
			}

	protected boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele,tagName));
	}

}