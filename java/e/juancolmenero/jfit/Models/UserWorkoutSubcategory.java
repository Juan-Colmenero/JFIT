package e.juancolmenero.jfit.Models;

public class UserWorkoutSubcategory {

    private String workoutName, sets, reps;
    private int exerciseID;

    public int getExerciseID(){return exerciseID;}

    public  void setExerciseID(int exerciseID){this.exerciseID = exerciseID;}

    public String getName() {
        return workoutName;
    }

    public void setName(String name) {
        this.workoutName = name;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

}
