package team.afalse.data_storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppypenko on 2/21/2017.
 */

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Tasks";

    private static final String TABLE_TASKS = "TaskTable";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_HOURS = "hours";
    private static final String KEY_MINUTES = "minutes";
    private static final String KEY_SECONDS = "seconds";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_COUNT_DOWN = "count_down";
    private static final String KEY_COMPLETION_DATE = "completion_date";
    private static final String KEY_DESCRIPTION = "description";

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void init() {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT, " + KEY_HOURS + " INTEGER, "
                + KEY_MINUTES + " INTEGER, " + KEY_SECONDS + " INTEGER, "
                + KEY_COMPLETED + " NUMERIC, " + KEY_COUNT_DOWN + " NUMERIC, "
                + KEY_COMPLETION_DATE + " TEXT )";
        getWritableDatabase().execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void drop() {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.GetTitle());
        values.put(KEY_DESCRIPTION, task.GetDescription());
        values.put(KEY_HOURS, task.GetTime()[0]);
        values.put(KEY_MINUTES, task.GetTime()[1]);
        values.put(KEY_SECONDS, task.GetTime()[2]);
        values.put(KEY_COMPLETED, task.GetCompleted());
        values.put(KEY_COUNT_DOWN, task.GetIsCountingDown());
        values.put(KEY_COMPLETION_DATE, task.GetCompletionDate());

        db.insertOrThrow(TABLE_TASKS, null, values);
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_HOURS, KEY_MINUTES, KEY_SECONDS,
                        KEY_COMPLETED, KEY_COUNT_DOWN, KEY_COMPLETION_DATE }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        int[] time = new int[]{Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5))};
        Task contact = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(8),
                time, Boolean.parseBoolean(cursor.getString(6)),
                Boolean.parseBoolean(cursor.getString(7)));

        return contact;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        boolean moveToFirst = cursor.moveToFirst();

        if (moveToFirst) {
            do {
                int hours = (int)Double.parseDouble(cursor.getString(3));
                int minutes = (int)Double.parseDouble(cursor.getString(4));
                int seconds = (int)Double.parseDouble(cursor.getString(5));
                int[] time = new int[]{hours, minutes, seconds};
                Task task = new Task();
                task.SetId(Integer.parseInt(cursor.getString(0)));
                task.SetTitle(cursor.getString(1));
                task.SetDescription(cursor.getString(2));
                task.SetTime(time);
                task.SetCompleted(Boolean.parseBoolean(cursor.getString(4)));
                task.SetIsCountingDown(Boolean.parseBoolean(cursor.getString(5)));
                task.SetCompletionDate(cursor.getString(6));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public int getTasksCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.GetTitle());
        values.put(KEY_HOURS, task.GetTime()[0]);
        values.put(KEY_MINUTES, task.GetTime()[1]);
        values.put(KEY_SECONDS, task.GetTime()[2]);
        values.put(KEY_COMPLETED, task.GetCompleted());
        values.put(KEY_COUNT_DOWN, task.GetIsCountingDown());
        values.put(KEY_COMPLETION_DATE, task.GetCompletionDate());
        values.put(KEY_DESCRIPTION, task.GetDescription());

        return db.update(DATABASE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.GetId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(task.GetId()) });
        db.close();
    }
}
