package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import e.juancolmenero.jfit.Models.UserWorkout;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class PickWorkoutActivity extends AppCompatActivity {
    private Intent intent;
    private int KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_workout);

        intent = getIntent();
        TextView bodyType_tv = findViewById(R.id.pick_body_lbl);
        //TextView date_tv = (TextView) findViewById(R.id.pick_date_selected);

        bodyType_tv.setText(intent.getStringExtra("body_type"));
        //date_tv.setText(intent.getStringExtra("date"));
        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();
    }

    public void OnSubmit(View view){
        UserWorkout userWorkout;
        String date = intent.getStringExtra("date");
        String body_type = intent.getStringExtra("body_type");

        final EditText workoutName_et = findViewById(R.id.workoutName_et);
        final EditText workoutSet_et = findViewById(R.id.pick_set_et);
        final EditText workoutRep_et = findViewById(R.id.pick_rep_et);

        String workoutName = workoutName_et.getText().toString();
        int sets = Integer.parseInt(workoutSet_et.getText().toString());
        int reps = Integer.parseInt(workoutRep_et.getText().toString());

        userWorkout = new UserWorkout(date, body_type, workoutName,
                sets, reps);

        sendWorkout(userWorkout);
        Intent postInent = new Intent(this, MainActivity.class);
        //PostFragment postFragment = new PostFragment();
        //postFragment.sendWorkout(userWorkout);
        //startActivity(postInent);
        postInent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(postInent);
        //this.finish();
    }

    public void sendWorkout(UserWorkout userWorkout){
        final String date, body, workoutName;
        final int sets, reps;
        final int userID;

        date = userWorkout.getDate();
        body = userWorkout.getBodyPart();
        workoutName = userWorkout.getWorkoutName();
        sets = userWorkout.getSets();
        reps = userWorkout.getReps();
        userID = KEY_ID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEND_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("body_part", body);
                params.put("workout_name", workoutName);
                params.put("sets", String.valueOf(sets));
                params.put("reps", String.valueOf(reps));
                params.put("user_id", String.valueOf(userID));
                return params;
            }

        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
