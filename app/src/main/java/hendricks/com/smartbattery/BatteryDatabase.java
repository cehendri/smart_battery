/**
 * Created by Christopher on 4/24/2015.
 */
package hendricks.com.smartbattery;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BatteryDatabase extends SQLiteOpenHelper{
    final static int statsDBVersion = 1;
    final static String statsDBName = "batterydatabase.s3db";
    private static final String tableName = "data";
    private static final String columnID = "_id";
    private static final String columnTime = "time";
    private static final String columnVoltage = "voltage";
    private static final String columnTemperature= "temperature";
    private static final String columnSOC = "soc";
    private static final String columnCurrent = "current";

    public BatteryDatabase(Context context) {
        super(context, statsDBName, null, statsDBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBatteryTable = "CREATE TABLE " + tableName + " (" + columnID
                + " INTEGER PRIMARY KEY," + columnTime + " REAL,"
                + columnVoltage + " REAL," + columnTemperature + " REAL,"
                + columnSOC + " REAL," + columnCurrent
                +" REAL);";
        db.execSQL(createBatteryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
        onCreate(db);
    }

    void addEntry(String time, double voltage, double temperature, double soc, double current){
        SQLiteDatabase db = this.getWritableDatabase();
        String insertSQL = "INSERT INTO " +  tableName + " (" + columnTime + "," +
                columnVoltage + "," + columnTemperature + "," + columnSOC +
                "," + columnCurrent + ") VALUES ('" + time +"','" +
                Double.toString(voltage) + "','" + Double.toString(temperature) + "','" + Double.toString(soc) +
                "','" + Double.toString(current) + "');";
        db.execSQL(insertSQL);
        Log.d("Executing:",insertSQL);
        db.close();
    }

//Need to test
    public int getEntries(){
        ArrayList<String> aList = new ArrayList<String>();
        String countEntries = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countEntries, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String currRow = cursor.getString(0) + "," +
                    cursor.getString(1) + "," +
                    cursor.getString(2) + "," +
                    cursor.getString(3) + "," +
                    cursor.getString(4);
            aList.add(currRow);
            cursor.moveToNext();
        }
        cursor.close();
        return cursor.getCount();
    }

}
