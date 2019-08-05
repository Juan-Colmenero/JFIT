package e.juancolmenero.jfit.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import e.juancolmenero.jfit.Adapter.NewsFeedPostAdapter;
import e.juancolmenero.jfit.Activities.EditProfileActivity;
import e.juancolmenero.jfit.Activities.FollowersActivity;
import e.juancolmenero.jfit.Models.PostBodyTypeModel;
import e.juancolmenero.jfit.Models.PostUserModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

/*
 * TODO: Display user posts if any is available.
 * if none leave bottom view blank
 * TODO:Display profile picture AND a new activity View to show user workouts from MY workouts link
 */

public class ProfileFragment extends Fragment {
    private static final String json_url = "http://www.juancolmenero.com/login_configuration.php";
    private TextView name, status, workouts, followers, following;
    private Button editProfileButton;
    private ImageView profilePicture;
    private int KEY_ID;
    private CollapsingToolbarLayout collapsingToolbar;

    //VerticalRecyclerViewAdapter adapter;
    private NewsFeedPostAdapter mAdapter;
    private ArrayList<PostUserModel> arrayListUserPost;
    private RequestQueue requestQueue;  // Assume this exists.

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: (1) CREATE JSON REQUEST THAT GO TO QUERY UTILS TO LOAD PROFILE

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        getProfileHeader();
        getPostData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //RecyclerView verticalRecyclerView;
        RecyclerView mPostRecyclerView;

        AppBarLayout appBarLayout;

        collapsingToolbar = rootView.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded);

        //This is the most important when you are putting custom textview in CollapsingToolbar
        //collapsingToolbar.setTitle(" ");

        appBarLayout = rootView.findViewById(R.id.appBarLayout);

        profilePicture = rootView.findViewById(R.id.profile_picture);
        name = rootView.findViewById(R.id.profile_name_lbl);
        status = rootView.findViewById(R.id.profile_status_lbl);
        //workouts = (TextView) getView().findViewById(R.id.profile_workout_count_lbl);
        followers = rootView.findViewById(R.id.profile_followers_count_lbl);
        following = rootView.findViewById(R.id.profile_following_count_lbl);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button);

        Uri uri = Uri.parse("http://www.juancolmenero.com/photos/about01.jpg");

        Glide.with(getContext())
                .load(uri)
                .apply(RequestOptions.circleCropTransform())
                .into(profilePicture);

        //arrayListVertical = new ArrayList<>();
        arrayListUserPost = new ArrayList<>();

        mPostRecyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        //mPostRecyclerView.setHasFixedSize(true);

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        mAdapter = new NewsFeedPostAdapter(getActivity(), arrayListUserPost);

        mPostRecyclerView.setAdapter(mAdapter);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    collapsingToolbar.setTitle(name.getText());
                } else if (verticalOffset == 0) {
                    // Expanded
                    collapsingToolbar.setTitle(" ");
                } else {
                    // Somewhere in between
                }
            }
        });





        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowers();
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowing();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditProfile();
            }
        });
    }

    private void onEditProfile(){
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.putExtra("user_name", status.getText().toString());
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("status", status.getText().toString());
        startActivity(intent);
    }

    private void onFollowers(){
        Intent intent = new Intent(getActivity(), FollowersActivity.class);
        intent.putExtra("request", "followers");
        startActivity(intent);
    }

    private void onFollowing(){
        Intent intent = new Intent(getActivity(), FollowersActivity.class);
        intent.putExtra("request", "following");
        startActivity(intent);
    }
    private void getProfileHeader(){



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            //converting response to json object
                            JSONObject baseObject = new JSONObject(response);
                            //getting the user from the response
                            JSONObject userJson = baseObject.getJSONObject("user");

                            //profilePicture.setImageDrawable(response.getString("profileImage"));

                            name.setText(userJson.getString("name"));
                            status.setText(userJson.getString("status"));
                            //workouts.setText(response.getString("workouts"));
                            followers.setText(userJson.getString("followers"));
                            following.setText(userJson.getString("following"));
                            requestQueue.stop();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Unable to perform volley request!!!!1");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getProfileFeed(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //profilePicture.setImageDrawable(response.getString("profileImage"));
                            name.setText(response.getString("name"));
                            status.setText(response.getString("status"));
                            //workouts.setText(response.getString("workouts"));
                            followers.setText(response.getString("followers"));
                            following.setText(response.getString("following"));
                            requestQueue.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("CANT PERFORM");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Unable to perform volley request!!!!1");
            }
        });

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void getPostData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_USER_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                JSONArray postsArray = userJson.getJSONArray("posts");

                                for(int i = 0; i<postsArray.length(); i++){
                                    JSONObject postObject = postsArray.getJSONObject(i);
                                    String picture = postObject.getString("picture");
                                    String username = postObject.getString("username");
                                    String status = postObject.getString("status");

                                    PostUserModel userPostInfoModel = new PostUserModel();
                                    userPostInfoModel.setName(username);
                                    userPostInfoModel.setPicture(picture);
                                    userPostInfoModel.setStatus(status);
                                    ArrayList<PostBodyTypeModel> arrayListVertical = new ArrayList<>();

                                    if(postObject.getJSONObject("workout").length() != 0 ){

                                        //GET WORKOUT
                                        JSONObject baseObject = postObject.getJSONObject("workout");

                                        Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<>();
                                        Iterator<String> iter = baseObject.keys();

                                        while (iter.hasNext()) {
                                            String key = iter.next();
                                            //PUSH DATA TO UI
                                            PostBodyTypeModel workoutListCategoryModel = new PostBodyTypeModel();
                                            workoutListCategoryModel.setBodyPart(key);

                                            //Just Body Part Names
                                            arrayListVertical.add(workoutListCategoryModel);

                                            JSONObject workoutArray = baseObject.getJSONObject(key);

                                            JSONArray actualWorkout = workoutArray.getJSONArray("workout");

                                            ArrayList<ArrayList<String>> myList = new ArrayList<>();

                                            String name, sets, reps;
                                            for(int x = 0; x < actualWorkout.length(); x++)
                                            {
                                                ArrayList<String> list=new ArrayList<>();//Creating arraylist
                                                JSONObject obj1 = actualWorkout.getJSONObject(x);
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

                                    }
                                    userPostInfoModel.setArrayList(arrayListVertical);
                                    arrayListUserPost.add(userPostInfoModel);
                                    mAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                System.out.println(obj.getString("message"));
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(KEY_ID));
                params.put("date", String.valueOf(KEY_ID));
                return params;
            }

        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
