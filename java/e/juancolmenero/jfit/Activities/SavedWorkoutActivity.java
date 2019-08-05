package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import e.juancolmenero.jfit.Adapter.SavedWorkoutAdapter;
import e.juancolmenero.jfit.Models.SavedWorkoutsModel;
import e.juancolmenero.jfit.Models.UserWorkout;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class SavedWorkoutActivity extends AppCompatActivity {
    //VerticalRecyclerViewAdapter adapter;
    private SavedWorkoutAdapter mAdapter;
    private String mDate;
    private ArrayList<SavedWorkoutsModel> arrayListWorkoutNames;
    private int name_id, KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_workout);

        RecyclerView mNamesRecyclerView;
        RequestQueue requestQueue;

        // Instantiate the cache & Set up the network to use HttpURLConnection as the HTTP client
        Cache cache = new DiskBasedCache(this.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name_id = getIntent().getIntExtra("name_id", 0);
        }
        System.out.println("HERE IS THE PASSED NAME_ID");
        System.out.println(name_id);

        Button addWorkout_btn;

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListWorkoutNames = new ArrayList<>();

        mNamesRecyclerView = findViewById(R.id.saved_workout_rv);
        mNamesRecyclerView.setHasFixedSize(true);
        mNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new SavedWorkoutAdapter(this, arrayListWorkoutNames);

        mNamesRecyclerView.setAdapter(mAdapter);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();
        getNamesList(name_id);

        addWorkout_btn = findViewById(R.id.addWorkouts_btn);
        addWorkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SavedWorkoutActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                sendToCalendarDialog(name_id);
            }
        });
    }

    private void removeAllItems() {
        arrayListWorkoutNames.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void getNamesList(final int name_id){
        //removeAllItems();
        System.out.println("HERE IS THE NAME_ID: " + name_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_SAVED_WORKOUTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //Log.d("TAG", obj.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(SavedWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //FIX API FOR FETCH, MAKE (OBJECT) on $RESULT if THERE IS DATA LIKE FETCHING user_post
                                //STORE KEYS AND LOOP THROUGH IT  TO ACCESS
                                Iterator<String> iter = userJson.keys();
                                while (iter.hasNext()) {
                                    SavedWorkoutsModel savedWorkoutsModel = new SavedWorkoutsModel();
                                    String key = iter.next();
                                    savedWorkoutsModel.setBodyPart(key);
                                    savedWorkoutsModel.setKEY_ID(KEY_ID);
                                    savedWorkoutsModel.setName_id(name_id);
                                    ArrayList<UserWorkoutSubcategory> arrayUserSubcategory = new ArrayList<>();

                                    JSONObject workoutArray = userJson.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");
                                    //ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();

                                    for(int i = 0; i < actualWorkout.length(); i++)
                                    {
                                        JSONObject obj1 = actualWorkout.getJSONObject(i);
                                        UserWorkoutSubcategory userWorkoutSubcategory = new UserWorkoutSubcategory();
                                        userWorkoutSubcategory.setExerciseID(obj1.getInt("exercise_id"));
                                        userWorkoutSubcategory.setName(obj1.getString("name"));
                                        userWorkoutSubcategory.setSets(Integer.toString(obj1.getInt("sets")));
                                        userWorkoutSubcategory.setReps(Integer.toString(obj1.getInt("reps")));
                                        arrayUserSubcategory.add(userWorkoutSubcategory);
                                    }
                                    savedWorkoutsModel.setArrayList(arrayUserSubcategory);
                                    arrayListWorkoutNames.add(savedWorkoutsModel);
                                    mAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(SavedWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TOAST A MESSAGE OR LOG TO SEE IF THERE IS AN ERROR
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("saved_name_id", String.valueOf(name_id));
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void sendNameList(final int name_id, final String date){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_SAVED_WORKOUTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(SavedWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //FIX API FOR FETCH, MAKE (OBJECT) on $RESULT if THERE IS DATA LIKE FETCHING user_post
                                //STORE KEYS AND LOOP THROUGH IT  TO ACCESS
                                Iterator<String> iter = userJson.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    JSONObject workoutArray = userJson.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");
                                    for(int i = 0; i < actualWorkout.length(); i++) {
                                        UserWorkout userWorkout;
                                        JSONObject obj1 = actualWorkout.getJSONObject(i);
                                       /* sendToCalendarDialog(key, obj1.getString("name"),
                                                Integer.toString(obj1.getInt("sets")),
                                                Integer.toString(obj1.getInt("reps")));*/
                                        userWorkout = new UserWorkout(date, key, obj1.getString("name"),
                                                obj1.getInt("sets"),
                                               obj1.getInt("reps"));
                                        sendWorkout(userWorkout);
                                    }
                                }

                            } else {
                                Toast.makeText(SavedWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TOAST A MESSAGE OR LOG TO SEE IF THERE IS AN ERROR
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("saved_name_id", String.valueOf(name_id));
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendToCalendarDialog(final int name_id){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_send_saved, null);

        // Create a calendar object and set year and month
        final Calendar mycal = new GregorianCalendar();
        final SimpleDateFormat dateFormatQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatQuery.setTimeZone(mycal.getTimeZone());
        final DatePicker datePicker = promptsView.findViewById(R.id.date_picker);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year = datePicker.getYear();
                                mycal.set(year, month, day);
                                mDate = dateFormatQuery.format(mycal.getTime());

                                System.out.println();
                                sendNameList(name_id, mDate);
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
