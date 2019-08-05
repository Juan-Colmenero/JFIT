package e.juancolmenero.jfit.Models;

public class UserWorkout {


    private String date;
    private String bodyPart, workoutName;
    private int sets, reps;

    public UserWorkout(String date, String bodyPart, String workoutName, int sets, int reps){
        this.date = date;
        this.bodyPart = bodyPart;
        this.workoutName = workoutName;
        this.sets = sets;
        this.reps = reps;
    }

    public String getDate() {
        return date;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

}
