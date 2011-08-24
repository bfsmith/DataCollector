package bs.howdy.DataCollector.Collectors.Gas;

import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import bs.howdy.DataCollector.Constants;

import android.util.Base64;
import android.util.Log;

public class GasCollectorUtility {
	private final String DESEDE_KEY = "zxasqwerdfcvtyghbnuijknm";
	private final String CHARSET = "UTF-8";
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd H:mm:ss.SSS");
	private DocumentBuilderFactory xmlFactory;
	private final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss.SSS");
	
	private static GasCollectorUtility _instance;
	
	private GasCollectorUtility() {
		xmlFactory = DocumentBuilderFactory.newInstance();
	}
	
	public static GasCollectorUtility getInstance() {
		if(_instance == null)
			_instance = new GasCollectorUtility();
		return _instance;
	}

	public byte[] encrypt3Des(String message) throws Exception {
		final byte[] keyBytes = DESEDE_KEY.getBytes(CHARSET);
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		final byte[] plainTextBytes = message.getBytes(CHARSET);
		final byte[] cipherText = cipher.doFinal(plainTextBytes);

		return cipherText;
	}

	public String decrypt3Des(byte[] message) throws Exception {
		final byte[] keyBytes = DESEDE_KEY.getBytes(CHARSET);
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		final byte[] plainText = decipher.doFinal(message);

		return new String(plainText, CHARSET);
	}

	public String encodeBase64(byte[] message) {
		String encodedMessage = Base64.encodeToString(message, Base64.DEFAULT);
		return encodedMessage;
	}

	public byte[] decodeBase64(String message) {
		byte[] decodedMessage = Base64.decode(message, Base64.DEFAULT);
		return decodedMessage;
	}

	public String getStationUrl(int id) {
		try {
//			StringBuilder sb = new StringBuilder();
//			sb.append(id);	// station Id
//			sb.append("|"); // divider
//			sb.append(""); 	// member_id
//			sb.append("|"); // divider
//			sb.append("GZNS14A8XP6YTcv6wZrGiw==");	// auth_id
//			sb.append("|");	// divider
//			sb.append(getTimeStamp());	// time stamp
			
//			String dataString = sb.toString();
			String dataString = id + "||GZNS14A8XP6YTcv6wZrGiw==|" + getTimeStamp();
			
			StringBuilder sb = new StringBuilder();
			sb.append("http://");	// Add protocol
			sb.append("xml.gasbuddy.com");	// host
			sb.append("/Mobile_GasBuddy_Details_Feed_v4.ashx");	// station_path
			sb.append("?q=");	// query prefix
			byte[] encryptedData = encrypt3Des(dataString);
			String encodedData = encodeBase64(encryptedData);
			sb.append(URLEncoder.encode(encodedData, CHARSET));
			
			String url = sb.toString();
			return url;
		} catch(Exception ex) {
			return null;
		}
	}
	
	private String getTimeStamp() {
		GregorianCalendar cal = new GregorianCalendar();
		TimeZone tz = TimeZone.getTimeZone("EST5EDT");
		TimeZone calTz = cal.getTimeZone();
		long millis = cal.getTimeInMillis();
		int differenceTz = calTz.getOffset(millis) - tz.getOffset(millis);
	
		millis = cal.getTimeInMillis() - (long)differenceTz;
		return DATE_FORMAT.format(new Date(millis));
	}
	
	public String streamToString(InputStream stream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
			    sb.append(line);
			}
		} catch(IOException ex) {}
		return sb.toString();
	}

	public String parseStationResponse(String response) {
		try {
			DocumentBuilder db = xmlFactory.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(response.getBytes(CHARSET)));
			Element docEle = doc.getDocumentElement();
			
			//Log.i(Constants.TAG, docEle.getLocalName());
			NodeList stations = docEle.getElementsByTagName("Station");
			if(stations != null && stations.getLength() > 0) {
				Element stationElement = (Element)stations.item(0);
				Station station = parseStation(stationElement);
				return station.toString();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(Constants.TAG, "Brokt");
		} 

		return "";
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
			Log.e(Constants.TAG, "Unable to parse a station tag... ");
			return null;
		}
		
		return new GasPrice(grade, price, date);
	}
}
