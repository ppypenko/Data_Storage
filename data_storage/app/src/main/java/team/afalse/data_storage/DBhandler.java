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
    private static final String KEY_TIME = "time";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_COUNT_DOWN = "count down";
    private static final String KEY_COMPLETION_DATE = "time";
    private static final String KEY_DESCRIPTION = "description";

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT, " + KEY_TIME + " TEXT, "
                + KEY_COMPLETED + " NUMBERIC, " + KEY_COUNT_DOWN + " NUMERIC, "
                + KEY_COMPLETION_DATE + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.GetTitle());
        values.put(KEY_DESCRIPTION, task.GetDescription());
        values.put(KEY_TIME, task.GetTime());
        values.put(KEY_COMPLETED, task.GetCompleted());
        values.put(KEY_COUNT_DOWN, task.GetIsCountingDown());
        values.put(KEY_COMPLETION_DATE, task.GetCompletionDate());

        db.insert(DATABASE_NAME, null, values);
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_NAME, new String[]{KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_TIME,
                        KEY_COMPLETED, KEY_COUNT_DOWN, KEY_COMPLETION_DATE }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        Task contact = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(6),
                Float.parseFloat(cursor.getString(3)), Boolean.parseBoolean(cursor.getString(4)),
                Boolean.parseBoolean(cursor.getString(5)));

        return contact;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();

        String selectQuery = "SELECT * FROM " + DATABASE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //cursor.getString(1), cursor.getString(2), cursor.getString(3),
        Float.parseFloat(cursor.getString(4)), Boolean.parseBoolean(cursor.getString(5)),
                Boolean.parseBoolean(cursor.getString(6))
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.SetId(Integer.parseInt(cursor.getString(0)));
                task.SetTitle(cursor.getString(1));
                task.SetDescription(cursor.getString(2));
                task.SetTime(Float.parseFloat(cursor.getString(3)));
                task.SetCompleted(Boolean.parseBoolean(cursor.getString(4)));
                task.SetIsCountingDown(Boolean.parseBoolean(cursor.getString(5)));
                task.SetCompletionDate(cursor.getString(6));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public int getTasksCount() {
        String countQuery = "SELECT * FROM " + DATABASE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.GetTitle());
        values.put(KEY_TIME, task.GetTime());
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
