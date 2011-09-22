package bs.howdy.DataCollector.Gas.Data;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bs.howdy.DataCollector.Gas.Constants;
import bs.howdy.DataCollector.Gas.GasGrade;
import bs.howdy.DataCollector.Gas.GasPrice;
import bs.howdy.DataCollector.Gas.Station;
import bs.howdy.DataCollector.Gas.StationProvider;

import android.util.Log;

public class StationFeedParser extends FeedParser {
	private StationProvider gdp;
	private GasPriceDataProvider gpdp; 
	
	private static StationFeedParser _instance;
	
	private StationFeedParser() {
		xmlFactory = DocumentBuilderFactory.newInstance();
		gdp = StationProvider.getInstance();
		gpdp = GasPriceDataProvider.getInstance();
	}
	
	public static StationFeedParser getInstance() {
		if(_instance == null)
			_instance = new StationFeedParser();
		return _instance;
	}
	
	public void parseStationResponse(String response) {
		try {
			DocumentBuilder db = xmlFactory.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(response.getBytes(Constants.CHARSET)));
			Element docEle = doc.getDocumentElement();
			
			NodeList stations = docEle.getElementsByTagName("Station");
			if(stations != null && stations.getLength() > 0) {
				Element stationElement = (Element)stations.item(0);
				parseStation(stationElement);
			}	
		} catch (Exception e) {
		}
	}
	
	private void parseStation(Element stationElement) {
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
			gdp.addStation(station);
		}
		
		for(GasGrade grade : GasGrade.values()) {
			GasPrice price = parseGasPrice(stationElement, grade, id);
			if(price != null && !gpdp.isAlreadyRecorded(price)) {
				gpdp.addGasPrice(price);
			}
		}
	}
	
	private GasPrice parseGasPrice(Element stationElement, GasGrade grade, int stationId) {
		float price;
		DateTime date;
		try {
			boolean hasPrice = getBooleanValue(stationElement, grade.getHasPriceTag());
			if(!hasPrice) return null;
			
			price = getFloatValue(stationElement, grade.getPriceTag());
			if(price <= 0)
				return null;
			date = getDateValue(stationElement, grade.getDateTag());
		} catch (Exception ex) {
			Log.e(bs.howdy.DataCollector.Constants.TAG, "Unable to parse a station tag... ");
			return null;
		}
		
		return new GasPrice(stationId, grade, price, date);
	}
}
