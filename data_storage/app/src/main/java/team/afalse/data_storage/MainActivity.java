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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBhandler db;
    private ListView taskList;
    private ArrayAdapter<Task> taskArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBhandler(this);
        db.init();
        taskList = (ListView)findViewById(R.id.allTasks);
        fillTask();
    }

    private void fillTask() {
        List<Task> tasks = db.getAllTasks();
        taskArrayAdapter = new ArrayAdapter<Task>(
                this, android.R.layout.simple_spinner_dropdown_item, tasks
        );
        taskList.setAdapter(taskArrayAdapter);
    }

    public void showNewTaskMenu(View view) {
        AnimHelper.animate(this, findViewById(R.id.hiddenEditMenu));
    }

    public void saveTask(View view) {
        String id = getEditTextString(R.id.taskId);
        if (id.isEmpty()) {
            makeNewTask();
        } else {
            editTask();
        }
        showNewTaskMenu(view);
    }

    private void makeNewTask() {
        String name = getEditTextString(R.id.taskName);
        String description = "";
        String completionDate = "";
        int[] time = new int[]{0};
        boolean completed = false;
        boolean isCountingDown = true;
        Task t = new Task(name, description, completionDate, time, completed, isCountingDown);
        db.addTask(t);
        fillTask();
        taskArrayAdapter.notifyDataSetChanged();
    }

    private void editTask() {

    }

    private String getEditTextString(int id) {
        return ((EditText)findViewById(id)).getText().toString();
    }

}
