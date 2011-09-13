package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bs.howdy.DataCollector.Collectors.Gas.Constants;
import bs.howdy.DataCollector.Collectors.Gas.Station;

public class ListFeedParser extends FeedParser {
	private static ListFeedParser _instance;
	StationFactory stationFactory = StationFactory.getInstance();
	
	private ListFeedParser() {
		xmlFactory = DocumentBuilderFactory.newInstance();
		stationFactory = StationFactory.getInstance();
	}
	
	public static ListFeedParser getInstance() {
		if(_instance == null)
			_instance = new ListFeedParser();
		return _instance;
	}
	
	public List<Station> parseStationResponse(String response) {
		ArrayList<Station> stations = new ArrayList<Station>();
		try {
			DocumentBuilder db = xmlFactory.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(response.getBytes(Constants.CHARSET)));
			Element docEle = doc.getDocumentElement();
			
			NodeList stationNodes = docEle.getElementsByTagName("Price");
			
			if(stationNodes != null) {
				for(int i = 0; i < stationNodes.getLength(); i++) {
					Element stationElement = (Element)stationNodes.item(i);
					Station station = parseStation(stationElement);
					if(station != null)
						stations.add(station);
				}
			}
		} catch (Exception e) {
		}
		return stations;
	}
	
	private Station parseStation(Element stationElement) {
		try {
			int id = getIntValue(stationElement, "StationId");
			String name = getTextValue(stationElement, "StationName");
			String location = getTextValue(stationElement, "Address") + " " +
					getTextValue(stationElement, "City") + " " +
					getTextValue(stationElement, "State") + " " +
					getTextValue(stationElement, "PostalCode");
			Station station = stationFactory.createStation(id, name, location);
			return station;
		} catch(Exception e) {
			return null;
		}
	}
}
