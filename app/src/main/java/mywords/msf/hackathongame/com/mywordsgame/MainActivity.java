package mywords.msf.hackathongame.com.mywordsgame;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private DBHelper dbHelper;
    private Button btn ;
    List<String> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        dbHelper = new DBHelper(this);
        if (checkDataBase() != true) {
            copyTextFileToDB();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*List<String> chk = new ArrayList<String>();
                chk = dbHelper.getAllComments();
                Log.i("chk Count=====", String.valueOf(chk));*/
                Cursor cd = dbHelper.getAllComments();
                for (cd.moveToFirst(); !(cd.isAfterLast()); cd.moveToNext()) {
                    String userCount = cd.getString(cd.getColumnIndex("name"));
                    questionList.add(userCount);
                }
                Log.i("chk Count=====", String.valueOf(questionList.size()));
            }
        });
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            checkDB = SQLiteDatabase.openDatabase(dbHelper.getDatabaseName(), null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e){
            //database doesn't exist yet
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void copyTextFileToDB() {
        InputStream inputStream = getResources().openRawResource(R.raw.englishwords);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String> value = new ArrayList<>();
        try {
            String line = "";
            while((line = br.readLine()) != null) {

                if (line.length() > 5 && line != null)
                   // message = line.split("\n");
                value.add(line.toString());

            }

            //Log.i("Count=====", String.valueOf(values.size()));
            try {
                dbHelper.insertAll(value);
                //db.insert(DBHelper.CONTACTS_TABLE_NAME, null, value);
                //dbHelper.insert(value);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.err.println("Parse Error: " + e.getMessage());
        }
    }
}
