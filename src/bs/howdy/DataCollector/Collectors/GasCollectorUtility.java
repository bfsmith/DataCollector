package bs.howdy.DataCollector.Collectors;

import static org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Base64;

public class GasCollectorUtility {
	private final String DESEDE_KEY = "zxasqwerdfcvtyghbnuijknm";
	private final String CHARSET = "UTF-8";
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd H:mm:ss.SSS");
	private XmlPullParserFactory xmlFactory;
	
	private static GasCollectorUtility _instance;
	
	private GasCollectorUtility() {	
		try {
			xmlFactory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			xmlFactory = null;
		}
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
			StringBuilder sb = new StringBuilder();
			sb.append(id);	// station Id
			sb.append("|"); // divider
			sb.append(""); 	// member_id
			sb.append("|"); // divider
			sb.append("GZNS14A8XP6YTcv6wZrGiw==");	// auth_id
			sb.append("|");	// divider
			sb.append(getTimeStamp());	// time stamp
			
			String dataString = sb.toString();
			
			sb = new StringBuilder();
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

	
	public String parseResponse(String response) {
		if(xmlFactory == null)
			return null;
		XmlPullParser xml;
		try {
			xml = xmlFactory.newPullParser();
			xml.setFeature(FEATURE_PROCESS_NAMESPACES, true);
			StringReader sr = new StringReader(response);
			xml.setInput(sr);

			
			
			
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
