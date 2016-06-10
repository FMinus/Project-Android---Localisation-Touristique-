package Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Models.Point;
import Models.User;

/**
 * Created by Ayoub on 5/6/2016.
 */
public class DatabaseHandlerLogin extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KhouribgaTourism";

    // Contacts table name
    private static final String TABLE = "Login";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";


    public DatabaseHandlerLogin(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT," + KEY_PASSWORD +" TEXT)";

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

    public void addrow(String username,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);


        // Inserting Row
        db.insert(TABLE, null, values);
        db.close();
    }

    public int updaterow(String id,String username,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);


        // updating row
        return db.update(TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<User> getAllUsers()
    {

        List<User> listUsers = new ArrayList<>();
        User u;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE, null);

        if (cursor .moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                String username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));

                u = new User(username,password);

                listUsers.add(u);

                cursor.moveToNext();
            }
        }

        return listUsers;
    }


    public User login(String username,String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor  cursor = db.rawQuery("select * from " + TABLE, null);
        String u , p;

        if (cursor .moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                u = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
                p = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));

                if(u.equals(username) && p.equals(password))
                {
                    return new User(u,p);
                }

                cursor.moveToNext();
            }
        }

        return null;
    }

    public void deleteTestMarkers()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+TABLE+" WHERE myValue LIKE 'New Marker%");
    }
}
