package mywords.msf.hackathongame.com.mywordsgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nandhinivm on 1/8/2016.
 */
public class SplashScreen extends Activity {

    ImageView image;
    TextView emoji_textView;
    int unicode = 0x1F609;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        image = (ImageView)findViewById(R.id.image);
        emoji_textView =(TextView)findViewById(R.id.emoji_textView);

        //Emoji
        emoji_textView.setText("Find Me... "+getEmijoByUnicode(unicode));

        // Zoomin and Zoomout Animation
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        image.setAnimation(zoomin);
        image.setAnimation(zoomout);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                   Intent intent = new Intent(SplashScreen.this,LevelScreen.class);
                   startActivity(intent);
                   finish();
            }
        }, SPLASH_TIME_OUT);
    }
    public String getEmijoByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}
