package mywords.msf.hackathongame.com.mywordsgame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private DBHelper dbHelper;
    private Button btn, loadlevel1 ;
    private List<String> questionList = new ArrayList<>();
    private TextView loadingtxt;
    private LinearLayout level1Layout;
    private String TAG = "MyWordsGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        loadlevel1 =  (Button)findViewById(R.id.loadlevel1);
        level1Layout = (LinearLayout)findViewById(R.id.level1Layout);
        loadingtxt = (TextView)findViewById(R.id.loadingtxt);

        dbHelper = new DBHelper(this);
        Log.i(TAG, String.valueOf(doesDatabaseExist(this, "MyDBName.db")));
        String chkDb =  String.valueOf(doesDatabaseExist(this, "MyDBName.db"));
        if (!chkDb.equalsIgnoreCase("true")) {
            copyTextFileToDB();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*List<String> chk = new ArrayList<String>();
                chk = dbHelper.getAllComments();
                Log.i("chk Count=====", String.valueOf(chk));*/
                //if (questionList.size() == 0){
                    Cursor cd = dbHelper.getAllComments();
                    for (cd.moveToFirst(); !(cd.isAfterLast()); cd.moveToNext()) {
                        String userCount = cd.getString(cd.getColumnIndex("name"));
                        questionList.add(userCount);
                    }
                //}
                if (questionList.size() > 0){
                    loadlevel1.setVisibility(View.VISIBLE);
                    loadingtxt.setVisibility(View.GONE);
                } /*else {
                    loadlevel1.setVisibility(View.GONE);
                    loadingtxt.setVisibility(View.VISIBLE);
                }*/
                //loadlevel1.setVisibility(View.VISIBLE);
                Log.i(TAG, String.valueOf(questionList.size()));
            }
        });

        loadlevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random rand = new Random();
                level1Layout.removeAllViews();
                final int N = 10;
                final TextView[] myTextViews = new TextView[N];
                for (int i =1; i<=5; i++){
                    String randomInt = questionList.get(rand.nextInt(questionList.size()));
                    Log.i(TAG, "Data========== "+ i +"====="+ randomInt);
                    final TextView rowTextView = new TextView(MainActivity.this);
                    rowTextView.setText( i + ")" + randomInt);
                    rowTextView.setTextColor(Color.BLACK);
                    rowTextView.setTextSize(20);
                    level1Layout.addView(rowTextView);
                    myTextViews[i] = rowTextView;
                }
            }
        });
    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    /*private boolean checkDataBase(){

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
    }*/

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
        Log.i(TAG, "Copying Files............");
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
