package mywords.msf.hackathongame.com.mywordsgame;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by nandhinivm on 1/8/2016.
 */
public class GridLevel extends Activity {
    GridView gv;
    Context context;
    ArrayList prgmName;
    public static String [] prgmNameList={"Level 1","Level 2","Level 3","Level 4","Level 5"};
    public static int [] prgmImages={R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
    String received = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlevel);
        Intent intent = getIntent();
        received = intent.getStringExtra("COMPLETED");
        gv=(GridView) findViewById(R.id.gridView1);
        gv.setAdapter(new CustomAdapter(GridLevel.this, prgmNameList,prgmImages, received));

    }

}

class CustomAdapter extends BaseAdapter{

    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    String checkReceived = null ;

    public CustomAdapter(GridLevel mainActivity, String[] prgmNameList, int[] prgmImages, String received) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkReceived = received;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.row_grid, null);
        holder.tv=(TextView) rowView.findViewById(R.id.item_text);
        holder.img=(ImageView) rowView.findViewById(R.id.item_image);

        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("vvvvvvv===="+ position);

                if (position == 0) {

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);

                } else if (position == 1 && checkReceived != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("LEVEL", "Level2");
                    context.startActivity(intent);
                } else if (position == 2 && checkReceived != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("LEVEL", "Level3");
                    context.startActivity(intent);
                } else if (position == 3 && checkReceived != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("LEVEL", "Level4");
                    context.startActivity(intent);
                } else if (position == 4 && checkReceived != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("LEVEL", "Level5");
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                }
            }
        });

        return rowView;
    }

}
