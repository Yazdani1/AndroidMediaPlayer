package yazdaniscodelab.androidmediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.File;
import java.io.InterruptedIOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

   static MediaPlayer mp;

    ArrayList<File>mySongs;

    Button btnPre,btnffpre,btnffnext,btnnext;

    ImageButton btnplay;

    SeekBar sb;
    int position;

    Uri u;

    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnplay=(ImageButton)findViewById(R.id.btnplay);
        btnPre=(Button)findViewById(R.id.btnpre);
        btnffpre=(Button)findViewById(R.id.btnffpre);
        btnffnext=(Button)findViewById(R.id.btnffnext);
        btnnext=(Button)findViewById(R.id.btnnext);

        sb=(SeekBar)findViewById(R.id.seekbar);


        updateseekBar=new Thread(){
            @Override
            public void run() {

                int currentDuration=mp.getDuration();

                int currentPosition=0;

                //sb.setMax(currentDuration);

                while (currentPosition<currentDuration){

                    try {

                        sleep(500);

                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);

                    }catch (InterruptedException e){

                        e.printStackTrace();

                    }

                }

               // super.run();
            }
        };


        btnPre.setOnClickListener(this);
        btnffpre.setOnClickListener(this);
        btnffnext.setOnClickListener(this);
        btnplay.setOnClickListener(this);
        btnnext.setOnClickListener(this);


        if (mp!=null){
            mp.stop();
            mp.release();
        }



        Intent i=getIntent();

        Bundle b=i.getExtras();

        mySongs=(ArrayList) b.getParcelableArrayList("songlist");

         position=b.getInt("pos",0);

         u=Uri.parse(mySongs.get(position).toString());

        mp=MediaPlayer.create(getApplicationContext(),u);

        mp.start();

        sb.setMax(mp.getDuration());

        updateseekBar.start();


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });


    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id){

            ///for song play and pause...
            case R.id.btnplay:

                if (mp.isPlaying()){
                    //btnplay.setText("||");
                    mp.pause();
                }

                else {
                    //btnplay.setText(">");
                    mp.start();
                }
                break;

            //for song previous skip..
            case R.id.btnffnext:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;

            case R.id.btnffpre:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;

            case R.id.btnnext:

                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

            case R.id.btnpre:

                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;



        }
    }


}
