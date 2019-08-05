package e.juancolmenero.jfit.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.juancolmenero.jfit.Models.CommentsModel;
import e.juancolmenero.jfit.Models.FollowsModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    Activity context;
    ArrayList<CommentsModel> arrayList;
    private int KEY_ID;

    public CommentsAdapter(Activity context, ArrayList<CommentsModel> arrayList){
        this.context=context;
        this.arrayList = arrayList;
    }

    @Override
    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(view.getContext().getApplicationContext()).getUser().getId();
        return new CommentsAdapter.CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsAdapter.CommentsViewHolder holder, final int position) {
        final CommentsModel commentsModel = arrayList.get(position);

        holder.userName.setText(commentsModel.getName());
        holder.commentMessage.setText(commentsModel.getCommentMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, userName;
        TextView commentMessage;
        Button comments_btn;
        public CommentsViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.comment_user_name);
            commentMessage = itemView.findViewById(R.id.comment);
        }
    }
}
