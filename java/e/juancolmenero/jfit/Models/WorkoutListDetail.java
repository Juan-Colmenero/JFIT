package e.juancolmenero.jfit.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutListDetail {

    WorkoutListDetail(String body, String name, String set, String rep){

    }

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> chest = new ArrayList<>();
        chest.add("Bench");
        chest.add("Cable Pulls");
        chest.add("Incline Press");
        chest.add("Dumbbell Press");

        List<String> Abs = new ArrayList<>();
        Abs.add("6 inch");
        Abs.add("Decline Leg raises ");
        Abs.add("Knee Tucks");
        Abs.add("Russian Twist");

        List<String> Shoulders = new ArrayList<>();
        Shoulders.add("Shoulder Shurs");
        Shoulders.add("Lateral Raises");
        Shoulders.add("Shoulder Press");
        Shoulders.add("Dumbbell Lateral raises");


        expandableListDetail.put("Chest", chest);
        expandableListDetail.put("Abs", Abs);
        expandableListDetail.put("Shoulders", Shoulders);

        /*
        TODO: pass key from JSON response to List<String> BODY_PART.key() = new ArrayList<String>();
        BODY_PART.key() add values that equal that key
        create other Key hashmaps if new body part is available
        after that add to  expandableListDetail.put("bodypart", bodyPart);
         */
        return expandableListDetail;

    }

}
