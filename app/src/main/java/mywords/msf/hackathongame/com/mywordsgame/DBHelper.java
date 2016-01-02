package mywords.msf.hackathongame.com.mywordsgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muthuv on 12/30/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    SQLiteDatabase db = this.getReadableDatabase();

    public DBHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table "+CONTACTS_TABLE_NAME+ "(id integer primary key, name text, charcount text)");
            System.out.println("In onCreate");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int numberOfRows(){

        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public void insertAll(List<String> objects){
        db.beginTransaction();
        for (int i=0;i<objects.size();i++){
            ContentValues contentValues = new ContentValues();
            // contentValues.put("_id", table.get_id());
            contentValues.put("name", objects.get(i).toString());
            contentValues.put("charcount", objects.get(i).length());
            db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        }
       /* for (String object : objects) {
            String table = object;
            ContentValues contentValues = new ContentValues();
            // contentValues.put("_id", table.get_id());
            contentValues.put("name", table);
            db.insert(CONTACTS_TABLE_NAME, null, this.getContentValues(object));
        }*/
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public void insert(List<String> value){
        ContentValues cv = new ContentValues();
        for (int i=0;i<value.size();i++){
            cv.put("name", value.get(i).toString());
            cv.put("charcount", value.get(i).length());
        }
        getWritableDatabase().insert(CONTACTS_TABLE_NAME, null, cv);
    }


    public ContentValues getContentValues(Object object) {
        String table = (String) object;
        ContentValues contentValues = new ContentValues();
        // contentValues.put("_id", table.get_id());
        contentValues.put("name", table);

        return contentValues;

    }

    public Cursor getAllComments() {
        String[] columns ={"name"};
       /* List<String> comments = new ArrayList<String>();
        String[] columns ={"name"};
        //Cursor findEntry = db.query(CONTACTS_TABLE_NAME, columns, "charcount is 6", new String[] { "name" }, null, null, null);
        //comments.add(findEntry.getString(findEntry.getColumnIndex("name")));
        String selectQuery = "select name from "+CONTACTS_TABLE_NAME+" where charcount is 6;";
        Cursor c = db.rawQuery(selectQuery, new String[]{"name"});
        //if (findEntry.moveToFirst()) {
            comments.add(c.getString(c.getColumnIndex("name")));
        //}
        c.close();
        return comments;*/

      /*  List<String> contacts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"name"};

        String selection =  "charcount is 6";

        String[] selectionArgs = {"charcount"};

        Cursor cursor = db.query(CONTACTS_TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                contacts.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));


            } while (cursor.moveToNext());

        }
        return contacts;*/
        Cursor c = db.query(CONTACTS_TABLE_NAME, // Table name
                columns, // String[] containing your column names
                "charcount is 6 AND ORDER BY RAND()", // your where statement, you do not include the WHERE or the FROM DATABASE_TABLE parts of the query,
                null,
                null,
                null,
                null
        );
        return c;
    }
}
