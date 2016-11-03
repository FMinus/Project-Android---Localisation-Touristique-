package Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Models.EventType;
import Models.Marker;

/**
 * Created by Ayoub on 5/6/2016.
 */
public class DatabaseHandlerMarkers extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KhouribgaTourism";

    // Contacts table name
    private static final String TABLE = "Evenements";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LOG = "longitude";
    private static final String KEY_EventType = "eventType";
    private static final String KEY_EventDESC = "eventDesc";


    private SQLiteDatabase db;

    public DatabaseHandlerMarkers(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_LAT +" NUMERIC(16,16),"
                + KEY_LOG +" NUMERIC(16,16), "
                + KEY_EventType + " TEXT, "
                +KEY_EventDESC+" TEXT"
                + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);

    }

    public void addrow(String name,double lat,double log, EventType eventType,String desc)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_EventDESC, desc);
        values.put(KEY_LAT, lat);
        values.put(KEY_LOG, log);
        values.put(KEY_EventType, eventType.toString());

        // Inserting Row
        db.insert(TABLE, null, values);
        db.close();
    }

    public void addMarker(Marker m)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEY_NAME, m.getEvent());
        values.put(KEY_LAT, m.getLat());
        values.put(KEY_LOG, m.getLog());
        values.put(KEY_EventType, m.getEventType().toString());
        values.put(KEY_EventDESC, m.getDesc());

        // Inserting Row
        db.insert(TABLE, null, values);
        db.close();
    }

    public int updaterow(String oldName,String newName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, newName);


        // updating row
        return db.update(TABLE, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(oldName) });
    }

    public List<Marker> getAllCoords()
    {

        List<Marker> listMarkers = new ArrayList<>();
        Marker p;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE, null);

        if (cursor .moveToFirst())
        {

            while (cursor.isAfterLast() == false)
            {
                String event = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String desc = cursor.getString(cursor.getColumnIndex(KEY_EventDESC));
                Double lat = cursor.getDouble(cursor.getColumnIndex(KEY_LAT));
                Double log = cursor.getDouble(cursor.getColumnIndex(KEY_LOG));
                String type = cursor.getString(cursor.getColumnIndex(KEY_EventType));

                EventType typeevent = EventType.valueOf(type);

                p = new Marker(event,typeevent,desc,lat,log);

                listMarkers.add(p);

                cursor.moveToNext();
            }
        }

        return listMarkers;
    }

    public List<Marker> getAllCoordsByEvent(String term)
    {

        List<Marker> listMarkers = new ArrayList<>();
        Marker p;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE +" WHERE "+KEY_NAME+"='"+term+"'", null);

        if (cursor .moveToFirst())
        {

            while (cursor.isAfterLast() == false)
            {
                String event = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                Double lat = cursor.getDouble(cursor.getColumnIndex(KEY_LAT));
                Double log = cursor.getDouble(cursor.getColumnIndex(KEY_LOG));
                String type = cursor.getString(cursor.getColumnIndex(KEY_EventType));
                String desc = cursor.getString(cursor.getColumnIndex(KEY_EventDESC));

                EventType typeEvent = EventType.valueOf(type);

                p = new Marker(event,typeEvent,desc,lat,log);


                listMarkers.add(p);

                cursor.moveToNext();
            }
        }

        return listMarkers;
    }


    // Getting single name
    public String getrow(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[]{KEY_ID, KEY_NAME, KEY_LAT, KEY_LOG}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(1);
    }

    public void deleteTestMarkers()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+TABLE+" WHERE "+KEY_NAME+" LIKE 'tm%");

    }

    public void deleteMarkersByName(String marker)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+TABLE+" WHERE "+KEY_NAME+"='"+marker+"'");

    }
}
