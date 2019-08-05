package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import e.juancolmenero.jfit.R;

public class PickBodyActivity extends AppCompatActivity {
    Intent pickWorkoutIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_body);

        pickWorkoutIntent = new Intent (this, PickWorkoutActivity.class);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        //pass on to new Activity
        pickWorkoutIntent.putExtra("date", date);
    }

    public void OnChest(View view){
        pickWorkoutIntent.putExtra("body_type", "Chest");
        startActivity(pickWorkoutIntent);
    }

    public void OnLegs(View view){
        pickWorkoutIntent.putExtra("body_type", "Legs");
        startActivity(pickWorkoutIntent);
    }

    public void OnBack(View view){
        pickWorkoutIntent.putExtra("body_type", "Back");
        startActivity(pickWorkoutIntent);
    }

    public void OnBiceps(View view){
        pickWorkoutIntent.putExtra("body_type", "Biceps");
        startActivity(pickWorkoutIntent);
    }

    public void OnTriceps(View view){
        pickWorkoutIntent.putExtra("body_type", "Triceps");
        startActivity(pickWorkoutIntent);
    }

    public void OnShoulders(View view){
        pickWorkoutIntent.putExtra("body_type", "Shoulders");
        startActivity(pickWorkoutIntent);
    }

    public void OnAbs(View view){
        pickWorkoutIntent.putExtra("body_type", "Abs");
        startActivity(pickWorkoutIntent);
    }

    public void OnOther(View view){
        pickWorkoutIntent.putExtra("body_type", "Other");
        startActivity(pickWorkoutIntent);
    }

}
