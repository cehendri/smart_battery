/**
 * Created by Christopher on 4/24/2015.
 */
package hendricks.com.smartbattery;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    void addEntry(double time, double voltage, double temperature, double soc, double current){
        SQLiteDatabase db = this.getWritableDatabase();
        String insertSQL = "INSERT INTO " +  tableName + " (" + columnTime + "," +
                columnVoltage + "," + columnTemperature + "," + columnSOC +
                "," + columnCurrent + ") VALUES ('" + Double.toString(time) +"','" +
                Double.toString(voltage) + "','" + Double.toString(temperature) + "','" + Double.toString(soc) +
                "','" + Double.toString(current) + "');";
        db.execSQL(insertSQL);
        Log.d("Executing:",insertSQL);
        db.close();
    }

    /*public int getWins(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countWins = "SELECT " + columnWin +" FROM " + tableName + " WHERE " + columnWin + "= \"y\";";
        Cursor cursor = db.rawQuery(countWins, null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }

    public int getLosses(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countLosses = "SELECT " + columnWin +" FROM " + tableName + " WHERE " + columnWin + "= \"n\";";
        Cursor cursor = db.rawQuery(countLosses, null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }
    //TODO Test this
    public float getAvgCornholes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String denom = "SELECT " + columnPlayerAvgCornholes + " FROM " + tableName + ";";
        Cursor cursor = db.rawQuery(denom,null);
        cursor.moveToFirst();
        int denomInt = cursor.getCount();
        cursor.close();
        String sumAvg = "SELECT SUM(" + columnPlayerAvgCornholes + ") FROM " + tableName + ";";
        Cursor cursorNum = db.rawQuery(sumAvg, null);
        cursorNum.moveToFirst();
        float numFloat = (float) cursorNum.getFloat(0);
        cursorNum.close();
        return numFloat/denomInt;
    }


    //TODO Test this
    public float getAvgOnBoard(){
        SQLiteDatabase db = this.getReadableDatabase();
        String denom = "SELECT " + columnPlayerAvgOnBoard + " FROM " + tableName + ";";
        Cursor cursor = db.rawQuery(denom,null);
        cursor.moveToFirst();
        int denomInt = cursor.getCount();
        cursor.close();
        String sumAvg = "SELECT SUM(" + columnPlayerAvgOnBoard + ") FROM " + tableName + ";";
        Cursor cursorNum = db.rawQuery(sumAvg, null);
        cursorNum.moveToFirst();
        float numFloat = (float) cursorNum.getFloat(0);
        cursorNum.close();
        return numFloat/denomInt;
    }


    public int getGames(){
        String countEntries = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countEntries, null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }*/

}
