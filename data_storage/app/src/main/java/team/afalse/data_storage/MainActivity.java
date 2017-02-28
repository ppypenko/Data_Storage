package team.afalse.data_storage;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements View.OnLongClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemLongClickListener{

    private DBhandler db;
    private ListView taskList;
    private ArrayAdapter<Task> taskArrayAdapter;
    private Task lastEditedTask;
    private static MainActivity reference;
    private static String todaysDate;
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBhandler(this);
        //db.init();
        taskList = (ListView)findViewById(R.id.allTasks);
        taskList.setOnItemClickListener(this);
        taskList.setOnItemLongClickListener(this);
        ((FloatingActionButton)findViewById(R.id.menu_button)).setOnLongClickListener(this);
        ((CheckBox)findViewById(R.id.cb_countingUp)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.paused)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.complete)).setOnCheckedChangeListener(this);
        EditText e1 = (EditText)findViewById(R.id.minutes);
        e1.setFilters(new InputFilter[]{new NumberFilter(0, 59)});
        EditText e2 = (EditText)findViewById(R.id.seconds);
        e2.setFilters(new InputFilter[]{new NumberFilter(0, 59)});
        reference = this;
        fillTask();
        Calendar c = Calendar.getInstance();
        setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        todaysDate = (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
        startTimer();
    }

    private void startTimer() {
        if (t == null) {
            System.out.println("Created a new timer!");
            t = new Timer();
            t.schedule(new TimerTask() {
                final ArrayAdapter<Task> adapter = taskArrayAdapter;
                @Override
                public void run() {
                    for (Task t : db.getAllTasks()) {
                        if (!t.isPaused() && isWithinLastDay(t) && !t.GetCompleted()) {
                            int[] time = t.GetTime();
                            int h = time[0], m = time[1], s = time[2];
                            if (h < 0 ||  m < 0 || s < 0){
                                t.SetCompleted(true);
                                time[0] = 0;
                                time[1] = 0;
                                time[2] = 0;
                                t.SetTime(time);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fillTask();
                                        taskArrayAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            else if (s <= 0) {
                                m -= 1;
                                s = 59;
                            }
                            else if (m <= 0) {
                                h -= 1;
                                m = 59;
                            }
                            else s -= 1;
                            time = new int[] {h, m, s};
                            t.SetTime(time);
                            db.updateTask(t);
                        } else if (isOverdue(t)) {
                            final Task task = t;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NotificationHelper.showNotification(getApplicationContext(), "Your task [" + task + "] is overdue!");
                                }
                            });
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillTask();
                            taskArrayAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    public static void setDate(int year, int month, int day) {
        String date = (month+1) + "/" + day + "/" + year;
        ((Button)reference.findViewById(R.id.taskCompletionDate)).setText(date);
    }

    private void fillTask() {
        List<Task> t = db.getAllTasks();
        final List<Task> tasks = t;
        taskArrayAdapter = new ArrayAdapter(this, R.layout.dropdown, tasks) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView)super.getView(position, convertView, parent);

                if (tasks.get(position).GetCompleted()) {
                    text.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (isWithinLastDay(tasks.get(position))) {
                    text.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (isOverdue(tasks.get(position))) {
                    text.setBackgroundColor(getResources().getColor(R.color.red));
                }

                return text;
            }
        };
        taskList.setAdapter(taskArrayAdapter);
    }

    public void selectDate(View view) {
        DialogFragment dateFragment = new CompletionDatePicker();
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    private boolean isWithinLastDay(Task t) {
        String taskCompletionDate = t.GetCompletionDate();
        String todaysDate = this.todaysDate;
        boolean isWithinLastDay = taskCompletionDate.equals(todaysDate);

        return isWithinLastDay;
    }

    private boolean isOverdue(Task t) {
        String completionDate = t.GetCompletionDate();
        String[] dateSplit = completionDate.split("/");
        int taskDate = Integer.parseInt(t.GetCompletionDate().split("/")[1]);
        int todayDate = Integer.parseInt(todaysDate.split("/")[1]);
        int taskH = t.GetTime()[0], taskM = t.GetTime()[1], taskS = t.GetTime()[2];
        boolean timerAtZero = (taskH == 0) && (taskM == 0) && (taskS == 0);
        boolean isOverdue = todayDate > taskDate || timerAtZero;
        return isOverdue;
    }

    private void resetEditMenu() {
        ((EditText)findViewById(R.id.taskName)).setText("");
        ((EditText)findViewById(R.id.taskDescription)).setText("");
        ((EditText)findViewById(R.id.taskId)).setText("");
        ((EditText)findViewById(R.id.hours)).setText("");
        ((EditText)findViewById(R.id.minutes)).setText("");
        ((EditText)findViewById(R.id.seconds)).setText("");
        ((Button)findViewById(R.id.taskCompletionDate)).setText(todaysDate);
        ((CheckBox)findViewById(R.id.cb_countingUp)).setChecked(false);
        ((CheckBox)findViewById(R.id.paused)).setChecked(false);
        ((CheckBox)findViewById(R.id.complete)).setChecked(false);
    }

    public void showNewTaskMenu(View view) {
        if (view != null && view.getId() == R.id.hiddenEditMenu) {
            resetEditMenu();
        }
        AnimHelper.animate(this, findViewById(R.id.hiddenEditMenu));
        if (findViewById(R.id.hiddenEditMenu).getVisibility() == View.VISIBLE) {
            startTimer();
        }
    }

    public void saveTask(View view) {
        String id = getEditTextString(R.id.taskId);
        if (id.isEmpty()) {
            makeNewTask();
        } else {
            editTask();
        }
        startTimer();
        fillTask();
        taskArrayAdapter.notifyDataSetChanged();
        showNewTaskMenu(view);
        resetEditMenu();
    }

    private void editTask() {
        lastEditedTask.SetTitle(getEditTextString(R.id.taskName));
        lastEditedTask.SetCompleted(((CheckBox)findViewById(R.id.complete)).isChecked());
        lastEditedTask.SetCompletionDate(((Button)findViewById(R.id.taskCompletionDate)).getText().toString());
        lastEditedTask.setPaused(((CheckBox)findViewById(R.id.paused)).isChecked());
        lastEditedTask.SetDescription(getEditTextString(R.id.taskDescription));
        int[] newTime = new int[]{
            Integer.parseInt(((EditText)findViewById(R.id.hours)).getText().toString()),
            Integer.parseInt(((EditText)findViewById(R.id.minutes)).getText().toString()),
            Integer.parseInt(((EditText)findViewById(R.id.seconds)).getText().toString()),
        };
        lastEditedTask.SetTime(newTime);
        db.updateTask(lastEditedTask);
    }

    private void makeNewTask() {
        String name = getEditTextString(R.id.taskName);
        String description = getEditTextString(R.id.taskDescription);
        String completionDate = ((Button)findViewById(R.id.taskCompletionDate)).getText().toString();
        int[] time = new int[] {
                Integer.parseInt(getEditTextString(R.id.hours)),
                Integer.parseInt(getEditTextString(R.id.minutes)),
                Integer.parseInt(getEditTextString(R.id.seconds))
        };
        boolean completed = ((CheckBox)findViewById(R.id.complete)).isChecked();
        boolean isCountingDown = ((CheckBox)findViewById(R.id.cb_countingUp)).isChecked();
        Task t = new Task(name, description, completionDate, time, completed, isCountingDown, false);
        db.addTask(t);
        fillTask();
        taskArrayAdapter.notifyDataSetChanged();
    }

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
        switch (buttonView.getId()) {
            case R.id.cb_countingUp:
                findViewById(R.id.taskCompletionDate).setVisibility((isChecked ? View.GONE : View.VISIBLE));
                findViewById(R.id.deadlineDate).setVisibility((isChecked ? View.GONE : View.VISIBLE));
                break;
            case R.id.paused:
//                lastEditedTask.setPaused(isChecked);
//                db.updateTask(lastEditedTask);
//                fillTask();
//                taskArrayAdapter.notifyDataSetChanged();
                break;
            case R.id.complete:
//                lastEditedTask.SetCompleted(isChecked);
//                db.updateTask(lastEditedTask);
//                fillTask();
//                taskArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task taskSelected = db.getAllTasks().get(position);
        lastEditedTask = taskSelected;
        setText(R.id.taskId, Integer.toString(taskSelected.GetId()));
        setText(R.id.taskName, taskSelected.GetTitle());
        ((Button)findViewById(R.id.taskCompletionDate)).setText(taskSelected.GetCompletionDate());
        setText(R.id.hours, Integer.toString(taskSelected.GetTime()[0]));
        setText(R.id.minutes, Integer.toString(taskSelected.GetTime()[1]));
        setText(R.id.seconds, Integer.toString(taskSelected.GetTime()[2]));
        ((CheckBox)findViewById(R.id.paused)).setChecked(taskSelected.isPaused());
        ((CheckBox)findViewById(R.id.complete)).setChecked(taskSelected.GetCompleted());
        if (t != null) {
            t.cancel();
            t = null;
        }
        showNewTaskMenu(null);
    }

    private void setText(int id, String text) {
        ((EditText)findViewById(id)).setText(text);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        db.deleteTask(db.getAllTasks().get(position));
        fillTask();
        taskArrayAdapter.notifyDataSetChanged();
        return false;
    }
}
