package team.afalse.data_storage;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnLongClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private DBhandler db;
    private ListView taskList;
    private ArrayAdapter<Task> taskArrayAdapter;
    private Task lastEditedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBhandler(this);
        db.init();
        taskList = (ListView)findViewById(R.id.allTasks);
        taskList.setOnItemClickListener(this);
        ((FloatingActionButton)findViewById(R.id.menu_button)).setOnLongClickListener(this);
        ((CheckBox)findViewById(R.id.cb_countingUp)).setOnCheckedChangeListener(this);
        EditText e1 = (EditText)findViewById(R.id.minutes);
        e1.setFilters(new InputFilter[]{new NumberFilter(0, 59)});
        EditText e2 = (EditText)findViewById(R.id.seconds);
        e2.setFilters(new InputFilter[]{new NumberFilter(0, 59)});
        fillTask();
    }

    private void fillTask() {
        List<Task> tasks = db.getAllTasks();
        taskArrayAdapter = new ArrayAdapter<Task>(
                this, android.R.layout.simple_spinner_dropdown_item, tasks
        );
        taskList.setAdapter(taskArrayAdapter);
        setTaskColors(tasks);
    }

    private void setTaskColors(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Task t  = tasks.get(i);
            if (t.GetCompleted()) {
                taskList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.green));
            } else if (isWithinLastDay(t.GetCompletionDate())) {

            }
        }
    }

    private boolean isWithinLastDay(String completionDate) {
        boolean isWithinLastDay = false;

        return isWithinLastDay;
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

    private void editTask() {
        db.updateTask(lastEditedTask);
    }

    private void makeNewTask() {
        String name = getEditTextString(R.id.taskName);
        String description = getEditTextString(R.id.taskDescription);
        String completionDate = getEditTextString(R.id.taskCompletionDate);
        int[] time = new int[]{0};
        boolean completed = false;
        boolean isCountingDown = true;
        Task t = new Task(name, description, completionDate, time, completed, isCountingDown);
        db.addTask(t);
        fillTask();
        taskArrayAdapter.notifyDataSetChanged();
    }

//<<<<<<< HEAD
//=======
//    private void editTask() {
//        int id = Integer.parseInt(getEditTextString(R.id.taskId));
//        String name = getEditTextString(R.id.taskName);
//        String description = getEditTextString(R.id.taskDescription);
//        String completionDate = getEditTextString(R.id.taskCompletionDate);
//    }
//>>>>>>> 0d837537252d2d076fddcab548bb4a248c85dc58

    private String getEditTextString(int id) {
        return ((EditText)findViewById(id)).getText().toString();
    }

    @Override
    public boolean onLongClick(View v) {
        AnimHelper.animate(this, findViewById(R.id.debugMenu));
        return false;
    }

    public void onDebugClick(View v) {
        switch (v.getId()) {
            case R.id.dropDatabase:
                db.drop();
                db.init();
                break;
            case R.id.testAlarm:
                AlarmHelper.getInstance().start();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task taskSelected = db.getAllTasks().get(position);
        lastEditedTask = taskSelected;
        setText(R.id.taskId, Integer.toString(taskSelected.GetId()));
        setText(R.id.taskName, taskSelected.GetTitle());
        showNewTaskMenu(null);
    }

    private void setText(int id, String text) {
        ((EditText)findViewById(id)).setText(text);
    }
}
