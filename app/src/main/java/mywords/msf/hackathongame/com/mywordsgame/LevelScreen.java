package mywords.msf.hackathongame.com.mywordsgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by nandhinivm on 1/8/2016.
 */
public class LevelScreen extends Activity {

    Button playgame,selectlevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelscreen);

        playgame = (Button)findViewById(R.id.playgame);
        selectlevel =(Button)findViewById(R.id.selectlevel);

        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelScreen.this,MainActivity.class);
                startActivity(intent);
            }
        });

        selectlevel.setOnClickListener(new View.OnClickListener() {
            @Override
                        public void onClick(View v) {
                Intent intent = new Intent(LevelScreen.this,GridLevel.class);
                startActivity(intent);
            }
        });

    }
}
