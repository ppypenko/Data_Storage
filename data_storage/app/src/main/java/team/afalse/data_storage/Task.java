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
}
