package bs.howdy.DataCollector.Gas.Data;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import bs.howdy.DataCollector.Gas.Constants;

import android.util.Base64;

public class WebUtility implements IWebHelper {
	private final String DESEDE_KEY = "zxasqwerdfcvtyghbnuijknm";
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd H:mm:ss.SSS");
	private final String AuthId = "GZNS14A8XP6YTcv6wZrGiw==";
	private static WebUtility _instance;
	
	private WebUtility() { }
	
	public static WebUtility getInstance() {
		if(_instance == null)
			_instance = new WebUtility();
		return _instance;
	}
	
	private byte[] encrypt3Des(String message) throws Exception {
		final byte[] keyBytes = DESEDE_KEY.getBytes(Constants.CHARSET);
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		final byte[] plainTextBytes = message.getBytes(Constants.CHARSET);
		final byte[] cipherText = cipher.doFinal(plainTextBytes);

		return cipherText;
	}

	@SuppressWarnings("unused")
	private String decrypt3Des(byte[] message) throws Exception {
		final byte[] keyBytes = DESEDE_KEY.getBytes(Constants.CHARSET);
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		final byte[] plainText = decipher.doFinal(message);

		return new String(plainText, Constants.CHARSET);
	}

	private String encodeBase64(byte[] message) {
		String encodedMessage = Base64.encodeToString(message, Base64.DEFAULT);
		return encodedMessage;
	}

	@SuppressWarnings("unused")
	private byte[] decodeBase64(String message) {
		byte[] decodedMessage = Base64.decode(message, Base64.DEFAULT);
		return decodedMessage;
	}

	public String getListUrl(int zipcode) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("");	// latitude
			sb.append("|"); // divider
			sb.append(""); 	// longitude
			sb.append("|"); // divider
			sb.append("");	// city
			sb.append("|"); // divider
			sb.append(""); 	// state
			sb.append("|"); // divider
			sb.append(zipcode); // zipcode
			sb.append("|");
			sb.append(5);	// Search Radius
			sb.append("|");
			sb.append(100);	// Hardcoded
			sb.append("|");
			sb.append("A");	// FuelPreference (empty string is acceptable)
			sb.append("|");
			sb.append("");	// member_id
			sb.append("|");
			sb.append(0);	// Hardcoded
			sb.append("|");
			sb.append("");	// Nothing, two pipes intentionally coded together o_O
			sb.append("|");
			sb.append(AuthId);	// auth_id
			sb.append("|");	// divider
			sb.append(1);	// Hardcoded
			sb.append("|");
			sb.append(3);	// Hardcoded
			sb.append("|");
			sb.append(getTimeStamp());	// time stamp
			//LONGITUDE|LATITUDE|||ZIP_CODE|5|100|A||0||AUTH_ID|1|3|TIME_STAMP
			
			String dataString = sb.toString();
			
			sb = new StringBuilder();
			sb.append("http://");	// Add protocol
			sb.append("xml.gasbuddy.com");	// host
			sb.append("/Mobile_GasBuddy_Station_Locator_Feed_v4.ashx");	// station_path
			sb.append("?q=");	// query prefix
			
			byte[] encryptedData = encrypt3Des(dataString);
			String encodedData = encodeBase64(encryptedData);
			sb.append(URLEncoder.encode(encodedData, Constants.CHARSET));
			
			String url = sb.toString();
			return url;
		} catch(Exception ex) {
			return null;
		}
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
			String dataString = id + "||" + AuthId + "|" + getTimeStamp();
			
			StringBuilder sb = new StringBuilder();
			sb.append("http://");	// Add protocol
			sb.append("xml.gasbuddy.com");	// host
			sb.append("/Mobile_GasBuddy_Details_Feed_v4.ashx");	// station_path
			sb.append("?q=");	// query prefix
			byte[] encryptedData = encrypt3Des(dataString);
			String encodedData = encodeBase64(encryptedData);
			sb.append(URLEncoder.encode(encodedData, Constants.CHARSET));
			
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
	
}
