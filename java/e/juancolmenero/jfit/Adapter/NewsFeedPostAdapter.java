package e.juancolmenero.jfit.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import e.juancolmenero.jfit.Activities.CommentsActivity;
import e.juancolmenero.jfit.Activities.DisplayUserWorkoutActivity;
import e.juancolmenero.jfit.Activities.FollowersActivity;
import e.juancolmenero.jfit.Models.PostBodyTypeModel;
import e.juancolmenero.jfit.Models.PostUserModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.FollowersRequest;
import e.juancolmenero.jfit.VolleyRequest.PostInteractionRequest;

public class NewsFeedPostAdapter extends RecyclerView.Adapter<NewsFeedPostAdapter.NewsFeedViewHolder> {
    private int KEY_ID;
    Activity context;
    ArrayList<PostUserModel> arrayList;

    public NewsFeedPostAdapter(Activity context, ArrayList<PostUserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_post_model,parent,false);
        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(view.getContext()).getUser().getId();
        return new NewsFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsFeedViewHolder holder, final int position) {
        final PostUserModel userPostInfoModel = arrayList.get(position);
        final PostInteractionRequest postInteractionRequest = new PostInteractionRequest(context, KEY_ID);
        postInteractionRequest.setPOST_ID(userPostInfoModel.getPost_id());
        postInteractionRequest.countLikes(new FollowersRequest.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(!jsonObject.getBoolean("error")){
                        if(jsonObject.getInt("like_count") >= 1){
                            holder.countLikes_lbl.setText(String.valueOf(jsonObject.getInt("like_count")));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //holder.bind(userPostInfo);
        String name = userPostInfoModel.getName();

        //CHECK IF STATUS IS EMPTY, IF SO MAKE VIEW GONE
        if(userPostInfoModel.getStatus().isEmpty() || userPostInfoModel.getStatus().length() == 0
        || userPostInfoModel.getStatus().equals("null")){
            holder.status.setVisibility(View.GONE);
        }else {
            String status = userPostInfoModel.getStatus();
            holder.status.setText(status);
        }

        //CHECK IF PICTURE IS EMPTY, IF SO MAKE VIEW GONE
        if(userPostInfoModel.getPicture().isEmpty() || userPostInfoModel.getPicture().length() == 0
                || userPostInfoModel.getPicture().equals("null")){
            holder.post_image.setVisibility(View.GONE);
        }else {
            Uri uri = Uri.parse(userPostInfoModel.getPicture());
            Glide
                    .with(context)
                    .load(uri)
                    .into(holder.post_image);
        }

        //LOOP THROUGH WORKOUT_ARRAY
        ArrayList<PostBodyTypeModel> singleItem = userPostInfoModel.getArrayList();
        if(singleItem.size() != 0) {
            String bodyGroupList = "";
            for (int i = 0; i < singleItem.size(); i++) {
                final PostBodyTypeModel workoutListCategoryModel = singleItem.get(i);

                String title = workoutListCategoryModel.getBodyPart();
                if (i != singleItem.size()) {
                    bodyGroupList += title + " ";
                }
            }

            holder.bodyPart.setText(bodyGroupList);
        }else {
            holder.bodyPart.setVisibility(View.GONE);
            holder.viewWorkoutHolder.setVisibility(View.GONE);
        }

        holder.name.setText(name);

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PostInteractionRequest postInteractionRequest = new PostInteractionRequest(context, KEY_ID);
                postInteractionRequest.setPOST_ID(userPostInfoModel.getPost_id());
                postInteractionRequest.likePost(new FollowersRequest.VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if(!jsonObject.getBoolean("error")){
                                System.out.println(jsonObject);
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                postInteractionRequest.countLikes(new FollowersRequest.VolleyCallback(){
                                    @Override
                                    public void onSuccess(String result){
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if(!jsonObject.getBoolean("error")){
                                                holder.countLikes_lbl.setText(String.valueOf(jsonObject.getInt("like_count")));
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        holder.like_lbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowersActivity.class);
                intent.putExtra("request", "likeList");
                intent.putExtra("post_id", userPostInfoModel.getPost_id());
                context.startActivity(intent);
            }
        });

        holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("request", "commentsList");
                intent.putExtra("post_id", userPostInfoModel.getPost_id());
                context.startActivity(intent);
            }
        });

        holder.comments_lbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("request", "commentsList");
                intent.putExtra("post_id", userPostInfoModel.getPost_id());
                context.startActivity(intent);
            }
        });

        holder.viewWorkoutHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayUserWorkoutActivity.class);
                intent.putExtra("date", userPostInfoModel.getDate());
                intent.putExtra("user_id", userPostInfoModel.getUser_id());
                context.startActivity(intent);
            }
        });


        //TODO: CREATE NEW VIEW AND HOW FULL WORKOUT WHEN BODY_VIEW PRESSED
        /*holder.bodyPart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                notifyItemChanged(position);
            }
        });*/

        /*NewsFeedPostCategoryAdapter newsFeedPostCategoryAdapter = new NewsFeedPostCategoryAdapter(context, singleItem);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        holder.recyclerView.setAdapter(newsFeedPostCategoryAdapter);*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class NewsFeedViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, status, bodyPart, countLikes_lbl, like_lbl, comments_lbl;
        ImageView post_image;
        ImageView btn_like, btn_comment;
        View subItem;
        LinearLayout viewWorkoutHolder;

        public NewsFeedViewHolder(View itemView)
        {
            super(itemView);

            //recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerViewWorkout);
            name = itemView.findViewById(R.id.user_name_post);
            post_image = itemView.findViewById(R.id.picture_post);
            status = itemView.findViewById(R.id.status_post);
            bodyPart = itemView.findViewById(R.id.bodyListString_tv);
            countLikes_lbl = itemView.findViewById(R.id.countLike_lbl);
            btn_like = itemView.findViewById(R.id.btn_like);
            btn_comment = itemView.findViewById(R.id.btn_comment);
            comments_lbl = itemView.findViewById(R.id.comment_lbl);
            like_lbl = itemView.findViewById(R.id.like_et);
            viewWorkoutHolder = itemView.findViewById(R.id.addWorkout_holder);
        }

        /*private void bind(PostUserModel verticalModel) {
            boolean expanded = verticalModel.isExpanded();

            status.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }*/
    }
}
