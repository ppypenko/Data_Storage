package team.afalse.data_storage;


/**
 * Created by Michael on 2/21/2017.
 */

public class Task {
    // title time description completed id

    private String title;
    // 0 is hours, 1 is minutes, 2 is seconds
    private int[] time = new int[3];
    private String description;
    private boolean completed;
    private int id;
    private String completionDate;
    private boolean isCountingDown;
    private boolean isPaused;
    public Task(){}

    public Task(int id, String title, String description, String completionDate, int[] time, boolean completed, boolean isCountingDown, boolean isPaused){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completionDate = completionDate;
        this.time = time;
        this.completed = completed;
        this.isCountingDown = isCountingDown;
        this.isPaused = isPaused;
    }

    public Task( String title, String description, String completionDate, int[] time, boolean completed, boolean isCountingDown, boolean isPaused){
        this.title = title;
        this.description = description;
        this.completionDate = completionDate;
        this.time = time;
        this.completed = completed;
        this.isCountingDown = isCountingDown;
        this.isPaused = isPaused;
    }

    public String GetTitle(){
        return title;
    }

    public void SetTitle(String p_title){
        title = p_title;
    }

    public int[] GetTime() {
        return time;
    }

    public void SetTime(int[] p_time){
        time = p_time;
    }

    public String GetDescription(){
        return description;
    }

    public void SetDescription(String p_description){
        description = p_description;
    }

    public boolean GetCompleted(){
        return completed;
    }

    public void SetCompleted(boolean p_completed){
        completed = p_completed;
    }

    public int GetId(){
        return id;
    }

    public void SetId(int p_id){
        id = p_id;
    }

    public String GetCompletionDate(){
        return completionDate;
    }

    public void SetCompletionDate(String p_completionDate) {
        completionDate = p_completionDate;
    }

    public boolean GetIsCountingDown(){
        return isCountingDown;
    }

    public void SetIsCountingDown(boolean p_countingDown){
        isCountingDown = p_countingDown;
    }

    @Override
    public String toString() {
        return title + " ( " + completionDate + " " + time[0] + ":" + time[1] + ":" + time[2] + " )";
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
