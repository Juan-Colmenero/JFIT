package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.PostWorkoutAdapter;
import e.juancolmenero.jfit.Adapter.SavedNameAdapter;
import e.juancolmenero.jfit.Models.PostUserModel;
import e.juancolmenero.jfit.Models.SavedNames;
import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class SavedNamesActivity extends AppCompatActivity {
    //VerticalRecyclerViewAdapter adapter;
    private SavedNameAdapter mAdapter;
    private ArrayList<SavedNames> arrayListWorkoutNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_names);

        int KEY_ID;

        //RecyclerView verticalRecyclerView;
        RecyclerView mNamesRecyclerView;
        RequestQueue requestQueue;  // Assume this exists.

        // Instantiate the cache
        Cache cache = new DiskBasedCache(this.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListWorkoutNames = new ArrayList<>();

        mNamesRecyclerView = findViewById(R.id.saved_names_rv);
        mNamesRecyclerView.setHasFixedSize(true);

        mNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new SavedNameAdapter(this, arrayListWorkoutNames);

        mNamesRecyclerView.setAdapter(mAdapter);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();
        getNamesList(KEY_ID);

    }

    private void removeAllItems() {
        arrayListWorkoutNames.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void getNamesList(final int KEY_ID){

        removeAllItems();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_SAVED_NAMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(SavedNamesActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONArray names_list = userJson.getJSONArray("list_of_names");
                                for(int i = 0; i<names_list.length(); i++){
                                    JSONObject objectName = names_list.getJSONObject(i);
                                    SavedNames savedNames = new SavedNames();
                                    savedNames.setName_id(objectName.getInt("id"));
                                    savedNames.setName(objectName.getString("workout_name"));
                                    savedNames.setUser_id(KEY_ID);

                                    arrayListWorkoutNames.add(savedNames);
                                    mAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(SavedNamesActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SavedNamesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
