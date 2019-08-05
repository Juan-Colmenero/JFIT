package e.juancolmenero.jfit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.juancolmenero.jfit.Models.PostBodyTypeModel;
import e.juancolmenero.jfit.R;

public class NewsFeedPostCategoryAdapter extends RecyclerView.Adapter<NewsFeedPostCategoryAdapter.CategoryViewHolder>  {
    Context context;
    ArrayList<PostBodyTypeModel> arrayList;


    public NewsFeedPostCategoryAdapter(Context context, ArrayList<PostBodyTypeModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_workout_category,parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final PostBodyTypeModel workoutListCategoryModel = arrayList.get(position);

        holder.bind(workoutListCategoryModel);
        String title = workoutListCategoryModel.getBodyPart();

        holder.textViewBodyPart.setText(title);

        holder.subItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,workoutListCategoryModel.getBodyPart(),Toast.LENGTH_SHORT).show();
                notifyItemChanged(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBodyPart;
        View subItem;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            textViewBodyPart = itemView.findViewById(R.id.listBodyPart);
            subItem = itemView.findViewById(R.id.workoutNameContainer);
        }

        private void bind(PostBodyTypeModel verticalModel) {
            boolean expanded = verticalModel.isExpanded();
            //subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }


}