package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import e.juancolmenero.jfit.Adapter.FollowsAdapter;
import e.juancolmenero.jfit.Models.FollowsModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.FollowersRequest;
import e.juancolmenero.jfit.VolleyRequest.PostInteractionRequest;

public class FollowersActivity extends AppCompatActivity {

    private int post_id;
    //VerticalRecyclerViewAdapter adapter;
    private FollowsAdapter mAdapter;
    private ArrayList<FollowsModel> arrayListFollows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        Bundle extras = getIntent().getExtras();
        String requestID = "";
        if (extras != null) {
            requestID = getIntent().getStringExtra("request");
            post_id = getIntent().getIntExtra("post_id", 0);
        }

        //RecyclerView verticalRecyclerView;
        RecyclerView mFollowsRecyclerView;
        int KEY_ID;


        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListFollows = new ArrayList<>();

        mFollowsRecyclerView = findViewById(R.id.follows_rv);
        mFollowsRecyclerView.setHasFixedSize(true);

        mFollowsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new FollowsAdapter(this, arrayListFollows);

        mFollowsRecyclerView.setAdapter(mAdapter);

        switch (requestID) {
            case "followers":{
                FollowersRequest followersRequest = new FollowersRequest(FollowersActivity.this, KEY_ID);
                followersRequest.getFollowers(new FollowersRequest.VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray followersArray = jsonObject.getJSONArray("followers");

                            for(int i = 0; i < followersArray.length(); i++){
                                JSONObject followers_info_object = followersArray.getJSONObject(i);
                                FollowsModel followsModel = new FollowsModel();
                                followsModel.setFollower_id(followers_info_object.getInt("follower_id"));
                                followsModel.setUserName(followers_info_object.getString("username"));
                                followsModel.setName(followers_info_object.getString("name"));
                                followsModel.setIsFollowing(followers_info_object.getString("following"));
                                followsModel.setRequestType("followers");
                                arrayListFollows.add(followsModel);
                            }

                            mAdapter.notifyDataSetChanged();
                            System.out.println(followersArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });}
                break;

            case "following":{
                //TODO: FIX, SAYS followersRequest is already defined above. Find out why and how to properly execute switch/case
                FollowersRequest followersRequest = new FollowersRequest(this, KEY_ID);
                followersRequest.getFollowing(new FollowersRequest.VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        try {

                            JSONObject jsonObject = new JSONObject(result);

                            JSONArray followersArray = jsonObject.getJSONArray("following");

                            for(int i = 0; i < followersArray.length(); i++){
                                JSONObject followers_info_object = followersArray.getJSONObject(i);
                                FollowsModel followsModel = new FollowsModel();
                                followsModel.setFollower_id(followers_info_object.getInt("follower_id"));
                                followsModel.setUserName(followers_info_object.getString("username"));
                                followsModel.setName(followers_info_object.getString("name"));
                                followsModel.setRequestType("following");
                                arrayListFollows.add(followsModel);
                            }

                            mAdapter.notifyDataSetChanged();

                            System.out.println(followersArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });}
                break;

            case "likeList":{
                PostInteractionRequest postInteractionRequest = new PostInteractionRequest(this, KEY_ID);
                postInteractionRequest.setPOST_ID(post_id);
                postInteractionRequest.getPostListLikes(new FollowersRequest.VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        System.out.println(result);
                        try {

                            JSONObject jsonObject = new JSONObject(result);

                            JSONArray followersArray = jsonObject.getJSONArray("likes_list");

                            for(int i = 0; i < followersArray.length(); i++){
                                JSONObject followers_info_object = followersArray.getJSONObject(i);
                                FollowsModel followsModel = new FollowsModel();
                                followsModel.setFollower_id(followers_info_object.getInt("user_id"));
                                followsModel.setUserName(followers_info_object.getString("username"));
                                followsModel.setName(followers_info_object.getString("name"));
                                followsModel.setRequestType("likeList");
                                arrayListFollows.add(followsModel);
                            }

                            mAdapter.notifyDataSetChanged();

                            System.out.println(followersArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });}
                break;
            default:
                break;
        }
    }
}
