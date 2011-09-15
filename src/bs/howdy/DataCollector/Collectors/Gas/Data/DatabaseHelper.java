package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
	protected Context context;
	private ArrayList<SchemaPatch> _patches;
	
	public DatabaseHelper(Context context) {
		super(context, Constants.Database.NAME, null, Constants.Database.VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		applyLatestSchemaPatches(db, 0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		applyLatestSchemaPatches(db);
	}
	
	private void applyLatestSchemaPatches(SQLiteDatabase db) {
		applyLatestSchemaPatches(db, getCurrentSchemaVersion(db));
	}

	private void applyLatestSchemaPatches(SQLiteDatabase db, int currentVersion) {
		ArrayList<SchemaPatch> schemaPatches = getPatches();
		for(SchemaPatch patch : schemaPatches) {
			if(patch.getNumber() > currentVersion) {
				 applySchemaPatch(db, patch);
			}
		}
	}
	
	private int getCurrentSchemaVersion(SQLiteDatabase db) {
		Log.v(Constants.TAG, "readonly = " + db.isReadOnly());
		Cursor cursor = db.rawQuery("SELECT CASE ( SELECT 1 FROM sqlite_master WHERE type='table' AND name='" + 
				Constants.Database.TABLE_SCHEMAPATCHES + "' ) WHEN 1 THEN (SELECT MAX(" +
				Constants.Database.COLUMN_SCHEMAPATCHES_PATCH + ") FROM " + 
				Constants.Database.TABLE_SCHEMAPATCHES  + ") ELSE 0 END;"
				, null);
		
		if(cursor == null)
			return 0;
		
		cursor.moveToFirst();
		int patchNumber = cursor.getInt(0);
		return patchNumber;
	}
	
	private void applySchemaPatch(SQLiteDatabase db, SchemaPatch patch) {
		 db.execSQL(patch.getSql());
		 
		 ContentValues values = new ContentValues();
		 values.put(Constants.Database.COLUMN_SCHEMAPATCHES_PATCH, patch.getNumber());
		 db.insert(Constants.Database.TABLE_SCHEMAPATCHES, null, values);
	}
	
	private ArrayList<SchemaPatch> getPatches() {
		if(_patches == null) {
			_patches = new ArrayList<SchemaPatch>();
			try {
				InputStream in = context.getResources().openRawResource(R.raw.sql);
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(in, null);
				doc.getDocumentElement().normalize();
				 
				Node schemaPatchNode = doc.getElementsByTagName("schemaPatch").item(0);
				Element schemaPatchElement = (Element) schemaPatchNode;
				NodeList patchList = schemaPatchElement.getElementsByTagName("patch");
		 
				for (int i = 0; i < patchList.getLength(); i++) {
			       _patches.add(new SchemaPatch(patchList.item(i)));  
				}
			} catch(Throwable t) {
				Toast.makeText(context, "Error loading db patches... " + t.toString(), Toast.LENGTH_SHORT);
			}
		}
		return _patches;
	}
	
	public class SchemaPatch {
		private int _number;
		private String _sql;
		
		public SchemaPatch(int number, String sql) {
			_number = number;
			_sql = sql;
		}
		
		public SchemaPatch(Node xmlNode) {
			Element element = (Element) xmlNode;
	        _number = Integer.parseInt(element.getAttribute("number")); 
	        _sql = element.getChildNodes().item(0).getNodeValue().trim();
		}
		
		public int getNumber() {
			return _number;
		}
		public String getSql() {
			return _sql;
		}
	}
}
