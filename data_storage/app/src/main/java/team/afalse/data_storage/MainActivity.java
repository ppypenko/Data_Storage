package team.afalse.data_storage;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

    public void showNewTaskMenu(View view) {
        AnimHelper.animate(this, findViewById(R.id.hiddenEditMenu));
    }

    public void saveTask(View view) {
        String id = ((EditText)findViewById(R.id.taskId)).getText().toString();
        if (id.isEmpty()) {
            makeNewTask();
        } else {
            editTask();
        }
    }

    private void makeNewTask() {

    }

    private void editTask() {
        
    }

}
