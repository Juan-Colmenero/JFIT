package e.juancolmenero.jfit.Models;

import android.widget.LinearLayout;
import android.widget.TextView;

public class SaveExerciseModel {

    private String workoutName;
    private String sets, reps;
    private int workout_id;
    private TextView name_tv;
    private TextView sets_tv;
    private TextView reps_tv;
    private LinearLayout holder;

    public SaveExerciseModel(int workout_id, String workoutName, String sets, String reps, TextView name_tv, TextView sets_tv, TextView reps_tv, LinearLayout holder){
        this.workout_id = workout_id;
        this.workoutName = workoutName;
        this.sets = sets;
        this.reps = reps;
        this.name_tv = name_tv;
        this.sets_tv = sets_tv;
        this.reps_tv = reps_tv;
        this.holder = holder;
    }

    public LinearLayout getHolder(){
        return holder;
    }
    public int getWorkout_id() {
        return workout_id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public TextView getName_tv() {
        return name_tv;
    }

    public TextView getSets_tv() {
        return sets_tv;
    }

    public TextView getReps_tv() {
        return reps_tv;
    }

}
