package Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Point;

/**
 * Created by Ayoub on 5/6/2016.
 */
public class DatabaseHandlerMarkers extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KhouribgaTourism";

    // Contacts table name
    private static final String TABLE = "Events";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";

    public DatabaseHandlerMarkers(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_X +" NUMERIC(16,16)," +  KEY_Y +" NUMERIC(16,16))";

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

    public void addrow(String name,double x,double y)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_X, x);
        values.put(KEY_Y, y);

        // Inserting Row
        db.insert(TABLE, null, values);
        db.close();
    }

    public int updaterow(String id,String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);


        // updating row
        return db.update(TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<Point> getAllCoords()
    {
        /*
        HashMap<String,HashMap<Double,Double>> coords = new HashMap<>();
        HashMap<Double,Double> tempCoord;
        */

        List<Point> listPoints = new ArrayList<>();
        Point p;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE, null);

        if (cursor .moveToFirst())
        {

            while (cursor.isAfterLast() == false)
            {
                String event = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                Double x = cursor.getDouble(cursor.getColumnIndex(KEY_X));
                Double y = cursor.getDouble(cursor.getColumnIndex(KEY_Y));

                p = new Point(event,x,y);

                listPoints.add(p);
                /*
                tempCoord = new HashMap<>();

                tempCoord.put(x,y);
                coords.put(event,tempCoord);
                */


                cursor.moveToNext();
            }
        }

        return listPoints;
    }

    // Getting single name
    public String getrow(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[]{KEY_ID, KEY_NAME, KEY_X, KEY_Y}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(1);
    }

    public void deleteTestMarkers()
    {

    }
}
