package e.juancolmenero.jfit.Models;

import java.util.ArrayList;

public class UserWorkoutCategory {
    private String bodyPart, date;
    private ArrayList<UserWorkoutSubcategory> arrayList;
    //state of the item
    private boolean expanded;
    private int KEY_ID;

    public int getKEY_ID(){ return KEY_ID; }

    public  void setKEY_ID(int KEY_ID){
        this.KEY_ID = KEY_ID;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
