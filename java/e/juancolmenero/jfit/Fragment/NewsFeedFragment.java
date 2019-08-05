package e.juancolmenero.jfit.Fragment;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.NewsFeedPostAdapter;
import e.juancolmenero.jfit.Models.PostBodyTypeModel;
import e.juancolmenero.jfit.Models.PostUserModel;
import e.juancolmenero.jfit.Models.WorkoutListDetail;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;


// TODO: fetch server data to display posts for news feed

public class NewsFeedFragment extends Fragment {

    private int KEY_ID;

    //RecyclerView verticalRecyclerView;
    RecyclerView mPostRecyclerView;

    //VerticalRecyclerViewAdapter adapter;
    NewsFeedPostAdapter mAdapter;
    ArrayList<PostUserModel> arrayListUserPost;

    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private RequestQueue requestQueue;  // Assume this exists.


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        getPostData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        //arrayListVertical = new ArrayList<>();
        arrayListUserPost = new ArrayList<>();

        mPostRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recyclerView);
        //mPostRecyclerView.setHasFixedSize(true);

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        mAdapter = new NewsFeedPostAdapter(getActivity(), arrayListUserPost);

        mPostRecyclerView.setAdapter(mAdapter);

        //setPostData();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public void getPostData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_ALL_POST,
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
                                    //System.out.println(postObject);
                                    int post_id = postObject.getInt("post_id");
                                    int user_id = postObject.getInt("user_id");
                                    String username = postObject.getString("username");
                                    String picture = postObject.getString("picture");
                                    String status = postObject.getString("status");
                                    String date = postObject.getString("date");

                                    PostUserModel userPostInfoModel = new PostUserModel();
                                    userPostInfoModel.setPost_id(post_id);
                                    userPostInfoModel.setDate(date);
                                    userPostInfoModel.setPicture(picture);
                                    userPostInfoModel.setUser_id(user_id);
                                    userPostInfoModel.setName(username);
                                    userPostInfoModel.setStatus(status);
                                    ArrayList<PostBodyTypeModel> arrayListVertical = new ArrayList<>();

                                    if(postObject.getJSONObject("workout").length() != 0 ){

                                        //GET WORKOUT
                                        JSONObject baseObject = postObject.getJSONObject("workout");

                                         Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<String, ArrayList<ArrayList<String>>>();
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

                                            ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();

                                            String name, sets, reps;
                                            for(int x = 0; x < actualWorkout.length(); x++)
                                            {
                                                ArrayList<String> list=new ArrayList<String>();//Creating arraylist
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
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //TODO: YOU DON"T REALLY NEED STRING REQUEST CHANGE TO JSON REQUEST, YOU DON"T NEED ANY PARAMETERS, FETCH ALL POSTS
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(KEY_ID));
                //params.put("date", String.valueOf(KEY_ID));
                return params;
            }

    };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    public void setPostData(){
        expandableListDetail = WorkoutListDetail.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        Set<String> setOfKeySet = expandableListDetail.keySet();

        List<String> listNames = Arrays.asList("Juan Colmenero", "Juan Colmenero", "Joseph Vaquez", "Eubert Almenar"
                ,"Juan Colmenero", "Juan Colmenero", "Juan Colmenero");
        List<String> listStatus = Arrays.asList("Let's gettit!!!!!", " ", " ","We can do this."
                ,"Work" ," Let's gettit!!!!!"," ");

        for(int i =0; listNames.size()>i; i++) {
            PostUserModel userPostInfoModel = new PostUserModel();
            userPostInfoModel.setName(listNames.get(i));
            userPostInfoModel.setStatus(listStatus.get(i));
            ArrayList<PostBodyTypeModel> arrayListVertical = new ArrayList<>();

            for (String key : setOfKeySet) {
                PostBodyTypeModel workoutListCategoryModel = new PostBodyTypeModel();
                workoutListCategoryModel.setBodyPart(key);

                //Just Body Part Names
                arrayListVertical.add(workoutListCategoryModel);
            }

            userPostInfoModel.setArrayList(arrayListVertical);
            arrayListUserPost.add(userPostInfoModel);
        }

        mAdapter.notifyDataSetChanged();

    }

}
