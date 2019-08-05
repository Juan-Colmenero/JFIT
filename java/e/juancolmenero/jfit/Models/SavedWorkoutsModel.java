package e.juancolmenero.jfit.Models;

import java.util.ArrayList;

public class SavedWorkoutsModel {
    private String bodyPart, date;
    private ArrayList<UserWorkoutSubcategory> arrayList;
    //state of the item
    private boolean expanded;
    private int KEY_ID, name_id;

    public int getName_id() {
        return name_id;
    }

    public void setName_id(int name_id) {
        this.name_id = name_id;
    }

    public int getKEY_ID(){ return KEY_ID; }

    public  void setKEY_ID(int KEY_ID){
        this.KEY_ID = KEY_ID;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public ArrayList<UserWorkoutSubcategory> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<UserWorkoutSubcategory> arrayList) {
        this.arrayList = arrayList;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

}
