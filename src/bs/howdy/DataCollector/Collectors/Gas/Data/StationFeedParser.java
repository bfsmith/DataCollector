package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bs.howdy.DataCollector.Collectors.Gas.Constants;
import bs.howdy.DataCollector.Collectors.Gas.GasGrade;
import bs.howdy.DataCollector.Collectors.Gas.GasPrice;
import bs.howdy.DataCollector.Collectors.Gas.Station;

import android.util.Log;

public class StationFeedParser {
	private DocumentBuilderFactory xmlFactory;
	private final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss.SSS");
	private StationDataProvider gdp;
	private GasPriceDataProvider gpdp; 
	
	private static StationFeedParser _instance;
	
	private StationFeedParser() {
		xmlFactory = DocumentBuilderFactory.newInstance();
		gdp = StationDataProvider.getInstance();
		gpdp = GasPriceDataProvider.getInstance();
	}
	
	public static StationFeedParser getInstance() {
		if(_instance == null)
			_instance = new StationFeedParser();
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
	private DateTime getDateValue(Element ele, String tagName) throws ParseException {
		Date d = XML_DATE_FORMAT.parse(getTextValue(ele,tagName));
		return new DateTime(d.getTime());
	}
	private boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele,tagName));
	}
	
	private Station parseStation(Element stationElement) {
		StationFactory stationFactory = StationFactory.getInstance();
		
		int id = getIntValue(stationElement, "StationId");
		
		Station station = gdp.getStation(id);
		
		if(station == null) {
			String name = getTextValue(stationElement, "StationName");
			String location = getTextValue(stationElement, "Address") + " " +
					getTextValue(stationElement, "City") + " " +
					getTextValue(stationElement, "State") + " " +
					getTextValue(stationElement, "PostalCode");
			station = stationFactory.createStation(id, name, location);
		}
		
		for(GasGrade grade : GasGrade.values()) {
			GasPrice price = parseGasPrice(stationElement, grade, id);
			if(price != null && !gpdp.isAlreadyRecorded(price)) {
				gpdp.addGasPrice(price);
				station.addGasPrice(price);
			}
		}
		
		return station;
	}
	
	private GasPrice parseGasPrice(Element stationElement, GasGrade grade, int stationId) {
		float price;
		DateTime date;
		try {
			boolean hasPrice = getBooleanValue(stationElement, grade.getHasPriceTag());
			if(!hasPrice) return null;
			
			price = getFloatValue(stationElement, grade.getPriceTag());
			date = getDateValue(stationElement, grade.getDateTag());
		} catch (Exception ex) {
			Log.e(bs.howdy.DataCollector.Constants.TAG, "Unable to parse a station tag... ");
			return null;
		}
		
		try {
			return new GasPrice(stationId, grade, price, date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
