package e.juancolmenero.jfit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import e.juancolmenero.jfit.Models.FollowsModel;
import e.juancolmenero.jfit.Models.PostBodyTypeModel;
import e.juancolmenero.jfit.Models.PostUserModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.FollowersRequest;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class FollowsAdapter extends RecyclerView.Adapter<FollowsAdapter.FollowersViewHolder>{
        Activity context;
        ArrayList<FollowsModel> arrayList;
    private int KEY_ID;

    public FollowsAdapter(Activity context, ArrayList<FollowsModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public FollowsAdapter.FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers_list,parent,false);
        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(view.getContext().getApplicationContext()).getUser().getId();
        return new FollowsAdapter.FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowsAdapter.FollowersViewHolder holder, final int position) {
        final FollowsModel followsModel = arrayList.get(position);

        holder.userName.setText(followsModel.getUserName());
        holder.name.setText(followsModel.getName());

        if (followsModel.getRequestType().equals("following")){
            holder.follow_btn.setText(R.string.unFollow);

        }else if(followsModel.getRequestType().equals("followers")){
            if(followsModel.getIsFollowing().equals("yes")){
                holder.follow_btn.setText(R.string.following);
            }else{
                holder.follow_btn.setText(R.string.follow);
            }

        }else if(followsModel.getRequestType().equals("likeList")){
                holder.follow_btn.setVisibility(View.GONE);
        }

        final FollowersRequest followersRequest = new FollowersRequest(context, KEY_ID);
        followersRequest.setFOLOWER_ID(followsModel.getFollowerId());
        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.follow_btn.getText().toString().equals("Following")){
                    followersRequest.unFollow(new FollowersRequest.VolleyCallback(){
                        @Override
                        public void onSuccess(String result){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if(!jsonObject.getBoolean("error")){
                                    holder.follow_btn.setText(R.string.follow);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if (holder.follow_btn.getText().toString().equals("Follow")){
                    followersRequest.follow(new FollowersRequest.VolleyCallback(){
                        @Override
                        public void onSuccess(String result){
                            try {
                                System.out.println(result);
                                JSONObject jsonObject = new JSONObject(result);
                                if(!jsonObject.getBoolean("error")){
                                    if(followsModel.getRequestType().equals("following")){
                                        holder.follow_btn.setText(R.string.unFollow);
                                    }else {
                                        holder.follow_btn.setText(R.string.following);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if (holder.follow_btn.getText().toString().equals("UnFollow")){
                    followersRequest.unFollow(new FollowersRequest.VolleyCallback(){
                        @Override
                        public void onSuccess(String result){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if(!jsonObject.getBoolean("error")){
                                    holder.follow_btn.setText(R.string.follow);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FollowersViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, userName;
        Button follow_btn;
        public FollowersViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.follows_name);
            userName = itemView.findViewById(R.id.follows_user_name);
            follow_btn = itemView.findViewById(R.id.button_follow);
        }
    }


}