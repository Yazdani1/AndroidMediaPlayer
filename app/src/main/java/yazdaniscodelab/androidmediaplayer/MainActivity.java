package yazdaniscodelab.androidmediaplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView)findViewById(R.id.lstplay);


        final ArrayList<File> mySongs=findsong(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];

        for (int i=0;i<mySongs.size();i++){
           //Toast.makeText(getApplicationContext(),mySongs.get(i).getName().toString(),Toast.LENGTH_LONG).show();

            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.song,R.id.text_xmlm,items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                intent.putExtra("pos",i);
                intent.putExtra("songlist",mySongs);
                startActivity(intent);

            }
        });


    }


    public ArrayList<File> findsong(File root){

        ArrayList<File>al=new ArrayList<File>();

        File[] files=root.listFiles();

        for (File singleFiles: files){

            if (singleFiles.isDirectory() && !singleFiles.isHidden()){
                al.addAll(findsong(singleFiles));
            }

            else {
                if (singleFiles.getName().endsWith(".mp3")){
                    al.add(singleFiles);
                }
            }

        }

        return al;

    }


}
