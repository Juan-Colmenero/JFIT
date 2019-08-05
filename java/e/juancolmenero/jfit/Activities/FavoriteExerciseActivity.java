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

import e.juancolmenero.jfit.Adapter.FavoriteExerciseAdapter;
import e.juancolmenero.jfit.Models.FavoriteExerciseModel;
import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class FavoriteExerciseActivity extends AppCompatActivity {

    //VerticalRecyclerViewAdapter adapter;
    private FavoriteExerciseAdapter mAdapter;
    private ArrayList<UserWorkoutCategory> arrayListUserWorkout;
    private int KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_exercise);

        //RecyclerView verticalRecyclerView;
        RecyclerView mPostRecyclerView;
        RequestQueue requestQueue;
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

        mPostRecyclerView = findViewById(R.id.favorite_recylcer_view);
        mPostRecyclerView.setHasFixedSize(true);

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new FavoriteExerciseAdapter(this, arrayListUserWorkout);

        mPostRecyclerView.setAdapter(mAdapter);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();

        getWorkout(intent.getStringExtra("date"), intent.getIntExtra("user_id", 1));
    }

    public void getWorkout(String date, final int user_id){
        removeAllItems();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_FAVORITE_EXERCISES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //Log.d("TAG", obj.toString());
                            System.out.println(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(FavoriteExerciseActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //FIX API FOR FETCH, MAKE (OBJECT) on $RESULT if THERE IS DATA LIKE FETCHING user_post
                                JSONObject baseObject = userJson.getJSONObject("favorite_exercise");

                                Map<String, ArrayList<ArrayList<FavoriteExerciseModel>>> workout_container = new HashMap<>();
                                Iterator<String> iter = baseObject.keys();

                                while (iter.hasNext()) {
                                    String key = iter.next();

                                    JSONObject workoutArray = baseObject.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");

                                    ArrayList<ArrayList<FavoriteExerciseModel>> myList = new ArrayList<>();

                                    String name;
                                    int exercise_id;
                                    for(int i = 0; i < actualWorkout.length(); i++)
                                    {
                                        FavoriteExerciseModel favoriteExerciseModel = new FavoriteExerciseModel();
                                        ArrayList<FavoriteExerciseModel> list=new ArrayList<>();//Creating arraylist
                                        JSONObject obj1 = actualWorkout.getJSONObject(i);
                                        name = obj1.getString("name");
                                        exercise_id = obj1.getInt("id");
                                        favoriteExerciseModel.setId(exercise_id);
                                        favoriteExerciseModel.setName(name);
                                        list.add(favoriteExerciseModel);
                                        myList.add(list);
                                        workout_container.put(key, myList);
                                    }
                                }

                                //SET DATA AND POPULATE ONTO RECYCLER VIEW
                                setData(workout_container);

                            } else {
                                Toast.makeText(FavoriteExerciseActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("user_id", String.valueOf(KEY_ID));
                //TODO: ADD BODY PART PARAM TO DISPLAY JUST THE BODY PART CLICKED FROM POST_FRAMGMENT ADAPTER, RETURN JUST ONE BODY WOKROUT PER QUERY
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void setData(Map<String, ArrayList<ArrayList<FavoriteExerciseModel>>> workout_container)
    {
        Set<String> setOfKeySet;
        setOfKeySet = workout_container.keySet();

        System.out.println(workout_container);
        for (String key : setOfKeySet) {
            UserWorkoutCategory workoutListCategoryModel = new UserWorkoutCategory();
            workoutListCategoryModel.setKEY_ID(KEY_ID);
            workoutListCategoryModel.setBodyPart(key);
            ArrayList<UserWorkoutSubcategory> arrayUserSubcategory = new ArrayList<>();

            for (ArrayList<FavoriteExerciseModel> workoutName : workout_container.get(key)) {
                //System.out.println("Workout - " + workoutName);
                FavoriteExerciseModel favoriteExerciseModel;
                favoriteExerciseModel = workoutName.get(0);
                UserWorkoutSubcategory userWorkoutSubcategory = new UserWorkoutSubcategory();
                userWorkoutSubcategory.setName(favoriteExerciseModel.getName());
                userWorkoutSubcategory.setExerciseID(favoriteExerciseModel.getId());
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
