package team.afalse.data_storage;


/**
 * Created by Michael on 2/21/2017.
 */

public class Task {
    // title time description completed id

    private String title;
    private float time;
    private String description;
    private boolean completed;
    private int id;
    private String completionDate;
    private boolean isCountingDown;
    public Task(){}

    public Task(int id, String title, String description, String completionDate, float time, boolean completed, boolean isCountingDown){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completionDate = completionDate;
        this.time = time;
        this.completed = completed;
        this.isCountingDown = isCountingDown;
    }

    public Task( String title, String description, String completionDate, float time, boolean completed, boolean isCountingDown){
        this.title = title;
        this.description = description;
        this.completionDate = completionDate;
        this.time = time;
        this.completed = completed;
        this.isCountingDown = isCountingDown;
    }

    public String GetTitle(){
        return title;
    }

    public void SetTitle(String p_title){
        title = p_title;
    }

    public float GetTime() {
        return time;
    }

    public void SetTime(float p_time){
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
}
