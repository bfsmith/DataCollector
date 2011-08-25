package bs.howdy.DataCollector.Collectors.Gas;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

public class StationParser {
	private DocumentBuilderFactory xmlFactory;
	private final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss.SSS");
	
	private static StationParser _instance;
	
	private StationParser() {
		xmlFactory = DocumentBuilderFactory.newInstance();
	}
	
	public static StationParser getInstance() {
		if(_instance == null)
			_instance = new StationParser();
		return _instance;
	}
	
	public Station parseStationResponse(String response) {
		try {
			DocumentBuilder db = xmlFactory.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(response.getBytes(Constants.CHARSET)));
			Element docEle = doc.getDocumentElement();
			
			//Log.i(Constants.TAG, docEle.getLocalName());
			NodeList stations = docEle.getElementsByTagName("Station");
			if(stations != null && stations.getLength() > 0) {
				Element stationElement = (Element)stations.item(0);
				Station station = parseStation(stationElement);
				return station;
			}
			
		} catch (Exception e) {
		} 

		return null;
	}
	
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	private int getIntValue(Element ele, String tagName) {
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	private float getFloatValue(Element ele, String tagName) {
		return Float.parseFloat(getTextValue(ele,tagName));
	}
	private Date getDateValue(Element ele, String tagName) throws ParseException {
		return XML_DATE_FORMAT.parse(getTextValue(ele,tagName));
	}
	private boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele,tagName));
	}
	
	private Station parseStation(Element stationElement) {
		StationFactory stationFactory = StationFactory.getInstance();
		
		int id = getIntValue(stationElement, "StationId");
		String name = getTextValue(stationElement, "StationName");
		String location = getTextValue(stationElement, "Address") + " " +
				getTextValue(stationElement, "City") + " " +
				getTextValue(stationElement, "State") + " " +
				getTextValue(stationElement, "PostalCode");
		Station station = stationFactory.getStation(id, name, location);
		
		for(GasGrade grade : GasGrade.values()) {
			GasPrice price = parseGasPrice(stationElement, grade);
			if(price != null) {
				station.addGasPrice(price);
			}
		}
		
		return station;
	}
	
	private GasPrice parseGasPrice(Element stationElement, GasGrade grade) {
		float price;
		Date date;
		try {
			boolean hasPrice = getBooleanValue(stationElement, grade.getHasPriceTag());
			if(!hasPrice) return null;
			
			price = getFloatValue(stationElement, grade.getPriceTag());
			date = getDateValue(stationElement, grade.getDateTag());
		} catch (Exception ex) {
			Log.e(bs.howdy.DataCollector.Constants.TAG, "Unable to parse a station tag... ");
			return null;
		}
		
		return new GasPrice(grade, price, date);
	}
}