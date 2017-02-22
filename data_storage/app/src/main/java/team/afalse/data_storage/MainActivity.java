package team.afalse.data_storage;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private DBhandler db;
    private ListView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBhandler(this);
        taskList = (ListView)findViewById(R.id.allTasks);
        fillTask();
    }

    private void fillTask() {
        ArrayAdapter<Task> taskArrayAdapter = new ArrayAdapter<Task>(
                this, android.R.layout.simple_spinner_dropdown_item, db.getAllTasks()
        );
        taskList.setAdapter(taskArrayAdapter);
    }

    private void alarm(){
        ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        tone.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
    }
}
