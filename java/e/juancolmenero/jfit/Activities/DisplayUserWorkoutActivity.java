package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.UserWorkoutAdapter;
import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class DisplayUserWorkoutActivity extends AppCompatActivity {

    //VerticalRecyclerViewAdapter adapter;
    private UserWorkoutAdapter mAdapter;

    private ArrayList<UserWorkoutCategory> arrayListUserWorkout;

    private int KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_workout);

        //RecyclerView verticalRecyclerView;
        RecyclerView mPostRecyclerView;

        RequestQueue requestQueue;  // Assume this exists.

        Intent intent = getIntent();

        intent.putExtra("view", "user_view");

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getApplication().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListUserWorkout = new ArrayList<>();

        mPostRecyclerView = (RecyclerView) findViewById(R.id.post_recylcer_view);
        mPostRecyclerView.setHasFixedSize(true);

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new UserWorkoutAdapter(this, arrayListUserWorkout);

        mPostRecyclerView.setAdapter(mAdapter);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();

        getWorkout(intent.getStringExtra("date"), intent.getIntExtra("user_id", 1));
    }

    public void getWorkout(String date, final int user_id){

        final String mDate = date;
        final int mUserId = user_id;

        removeAllItems();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //Log.d("TAG", obj.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(DisplayUserWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                userJson.getString("date");
                                //FIX API FOR FETCH, MAKE (OBJECT) on $RESULT if THERE IS DATA LIKE FETCHING user_post
                                JSONObject baseObject = userJson.getJSONObject("workout");

                                Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<>();
                                Iterator<String> iter = baseObject.keys();

                                while (iter.hasNext()) {
                                    String key = iter.next();

                                    JSONObject workoutArray = baseObject.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");

                                    ArrayList<ArrayList<String>> myList = new ArrayList<>();

                                    String name, sets, reps;
                                    for(int i = 0; i < actualWorkout.length(); i++)
                                    {
                                        ArrayList<String> list=new ArrayList<>();//Creating arraylist
                                        JSONObject obj1 = actualWorkout.getJSONObject(i);
                                        name = obj1.getString("name");
                                        sets = Integer.toString(obj1.getInt("sets"));
                                        reps = Integer.toString(obj1.getInt("reps"));
                                        list.add(name);
                                        list.add(sets);
                                        list.add(reps);
                                        myList.add(list);
                                        workout_container.put(key, myList);
                                    }
                                }

                                //SET DATA AND POPULATE ONTO RECYCLER VIEW
                                setData(workout_container);

                            } else {
                                Toast.makeText(DisplayUserWorkoutActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("date", mDate);
                params.put("user_id", String.valueOf(user_id));
                //TODO: ADD BODY PART PARAM TO DISPLAY JUST THE BODY PART CLICKED FROM POST_FRAMGMENT ADAPTER, RETURN JUST ONE BODY WOKROUT PER QUERY
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void setData(Map<String, ArrayList<ArrayList<String>>> workout_container)
    {
        Set<String> setOfKeySet;
        setOfKeySet = workout_container.keySet();

        for (String key : setOfKeySet) {
            UserWorkoutCategory workoutListCategoryModel = new UserWorkoutCategory();
            workoutListCategoryModel.setBodyPart(key);
            ArrayList<UserWorkoutSubcategory> arrayUserSubcategory = new ArrayList<>();

            for (ArrayList<String> workoutName : workout_container.get(key)) {
                //System.out.println("Workout - " + workoutName);
                UserWorkoutSubcategory userWorkoutSubcategory = new UserWorkoutSubcategory();
                userWorkoutSubcategory.setName(workoutName.get(0));
                userWorkoutSubcategory.setSets(workoutName.get(1));
                userWorkoutSubcategory.setReps(workoutName.get(2));
                arrayUserSubcategory.add(userWorkoutSubcategory);
            }

            workoutListCategoryModel.setArrayList(arrayUserSubcategory);
            arrayListUserWorkout.add(workoutListCategoryModel);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void removeAllItems() {
        arrayListUserWorkout.clear();
        mAdapter.notifyDataSetChanged();
    }
}
