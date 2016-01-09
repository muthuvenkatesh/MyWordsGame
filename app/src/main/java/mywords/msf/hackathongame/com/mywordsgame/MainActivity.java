package mywords.msf.hackathongame.com.mywordsgame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends Activity implements TextWatcher{

    private DBHelper dbHelper;
    private Button btn, loadlevel1, validate ;
    private List<String> questionList = new ArrayList<>();
    private TextView loadingtxt;
    private LinearLayout level1Layout, progressBarLayout;
    private String TAG = "MyWordsGame";
    private String answerStr = " ", inputStr = " ",  randomInt = " ";
    private ImageView imageResult;
    private TextView textResult, text;
    private MyCountDownTimer countDownTimer;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    private boolean timerHasStarted = false;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        loadlevel1 =  (Button)findViewById(R.id.loadlevel1);
        level1Layout = (LinearLayout)findViewById(R.id.level1Layout);
        validate = (Button)findViewById(R.id.submit);
        imageResult = (ImageView)findViewById(R.id.imageResult);
        textResult = (TextView)findViewById(R.id.textResult);
        progressBarLayout = (LinearLayout)findViewById(R.id.progressBarLayout);
        text = (TextView) this.findViewById(R.id.timer);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setClickable(false);
        //ratingBar.setEnabled(false);

        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime / 1000));

        dbHelper = new DBHelper(this);
        Log.i(TAG, String.valueOf(doesDatabaseExist(this, "MyDBName.db")));
        String chkDb =  String.valueOf(doesDatabaseExist(this, "MyDBName.db"));
        if (!chkDb.equalsIgnoreCase("true")) {
            copyTextFileToDB();
        }

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!btn.getText().toString().equalsIgnoreCase("End Game")) {
//                    //progressBarLayout.setVisibility(View.VISIBLE);
                    loadLevel1Data();
                    Log.i(TAG, "If loop......" + String.valueOf(questionList.size()));
//                } else {
//                    Log.i(TAG, String.valueOf(questionList.size()));
//                    finish();
//                }
//
//            }
//        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Answer value ====="+  answerStr.toString());
                System.out.println("input  value ====="+ inputStr.toString());
                System.out.println("random  value ====="+ randomInt.toString());

                String str = inputStr.replace("_", answerStr.trim());
                if (str.equalsIgnoreCase(randomInt)){
                    System.out.println("Success =====" + str);
                    textResult.setVisibility(View.VISIBLE);
                    imageResult.setVisibility(View.VISIBLE);
                    imageResult.setImageResource(R.drawable.yes);
                    textResult.setText("Perfect !!!");
                    timerHasStarted = false;
                    loadQuestion();
                    if (ratingBar.getNumStars() >= 1){
                        ratingBar.setRating(ratingBar.getNumStars()+1);
                    } else {
                        ratingBar.setRating(1);
                    }
                    //ratingBar.setNumStars(1);
                } else {
                    System.out.println("Failed =====" + str);
                    textResult.setVisibility(View.VISIBLE);
                    imageResult.setVisibility(View.VISIBLE);
                    imageResult.setImageResource(R.drawable.no);
                    textResult.setText("Try Again !!!");
                    if (text.toString().equalsIgnoreCase("Time's Up")) {
                        validate.setEnabled(false);
                        timerHasStarted = false;
                        loadlevel1.setText("Next >");
                        loadlevel1.setEnabled(true);
                    }
                }
            }
        });

        //final EditText myTextViews = new EditText(MainActivity.this);
        loadlevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestion();
                //if (!timerHasStarted) {
                    countDownTimer.start();
                    timerHasStarted = true;
                //}
            }
        });
    }

    private void loadQuestion() {
        loadlevel1.setText("Next >");
        loadlevel1.setEnabled(false);
        if (questionList != null & questionList.size() > 0) {
            Random rand = new Random();
            level1Layout.removeAllViews();

            textResult.setVisibility(View.GONE);
            imageResult.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            if (!timerHasStarted) {
                countDownTimer.start();
                timerHasStarted = true;
            }

            randomInt = questionList.get(rand.nextInt(questionList.size()));
            inputStr = changeString(randomInt);
            for (int i = 0; i < inputStr.length(); i++) {
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                //linearLayout.setGravity(Gravity.CENTER);
                EditText myTextViews = new EditText(MainActivity.this);
                       /* int maxLength = 1;
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                        myTextViews.setFilters(FilterArray);*/
                //myTextViews.addTextChangedListener(MainActivity.this);
                myTextViews.setPadding(30, 10, 30, 10);
                myTextViews.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_file));
                //Log.i(TAG, "Data========== " + inputStr.charAt(i));
                if (String.valueOf(inputStr.charAt(i)).equalsIgnoreCase("_")) {
                    myTextViews.setText(" ");
                    myTextViews.setEnabled(true);
                    myTextViews.addTextChangedListener(MainActivity.this);
                } else {
                    myTextViews.setText(String.valueOf(inputStr.charAt(i)));
                    myTextViews.setEnabled(false);
                }

                linearLayout.addView(myTextViews);
                level1Layout.addView(linearLayout);
            }
        } else {
            loadLevel1Data();
        }
    }

    String changeString(String s) {

        char[] characters = s.toCharArray();
        System.out.println("NNN String characters= " + characters);

        int rand = (int)(Math.random() * s.length());
        System.out.println("NNN String rand = " + rand);

        characters[rand] = '_';
        System.out.println("NNN String characters[rand] = " + characters[rand]);

        return new String(characters);

    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void loadLevel1Data(){
        //progressBarLayout.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (questionList.size() == 0){
                    Cursor cd = dbHelper.getAllComments();
                    for (cd.moveToFirst(); !(cd.isAfterLast()); cd.moveToNext()) {
                        String userCount = cd.getString(cd.getColumnIndex("name"));
                        questionList.add(userCount);
                    }
                   // handler.sendMessage(handler.obtainMessage());
                    cd.close();

                }
            }
        }).start();
    }

   /* Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            btn.setText("End Game");
            btn.setTextColor(Color.RED);
            progressBarLayout.setVisibility(View.GONE);
            loadlevel1.setVisibility(View.VISIBLE);
        }
    };*/

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().trim().length() > 0) {
                answerStr = s.toString();
                validate.setEnabled(true);
            } else {
                validate.setEnabled(false);
            }

        imageResult.setVisibility(View.GONE);
        textResult.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text.setText("Time's up!");
            validate.setEnabled(false);
            loadlevel1.setEnabled(true);
            imageResult.setVisibility(View.GONE);
            textResult.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }
    }
}
