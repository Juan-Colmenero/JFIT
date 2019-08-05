package e.juancolmenero.jfit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;

public class UserWorkoutAdapter extends RecyclerView.Adapter<UserWorkoutAdapter.WorkoutViewHolder> {

    Context context;
    ArrayList<UserWorkoutCategory> arrayList;

    public UserWorkoutAdapter(Context context, ArrayList<UserWorkoutCategory> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public UserWorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_workout_category,parent,false);
        return new UserWorkoutAdapter.WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserWorkoutAdapter.WorkoutViewHolder holder, final int position) {

        final UserWorkoutCategory userWorkoutCategory = arrayList.get(position);
        int KEY_ID = userWorkoutCategory.getKEY_ID();

        holder.bind(userWorkoutCategory);
        String title = userWorkoutCategory.getBodyPart();
        ArrayList<UserWorkoutSubcategory> singleItem = userWorkoutCategory.getArrayList();

        holder.textViewBodyPart.setText(title);
        UserSubWorkoutAdapter postWorkoutSubcategory = new UserSubWorkoutAdapter(context, singleItem, userWorkoutCategory.getBodyPart(),userWorkoutCategory.getDate(), KEY_ID);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        holder.recyclerView.setAdapter(postWorkoutSubcategory);

        holder.subItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,userWorkoutCategory.getBodyPart(),Toast.LENGTH_SHORT).show();
                boolean expanded = userWorkoutCategory.isExpanded();

                userWorkoutCategory.setExpanded(!expanded);
                if(!expanded){
                    holder.subItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                notifyItemChanged(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder
    {
        RecyclerView recyclerView;
        TextView textViewBodyPart;
        View subItem;

        public WorkoutViewHolder(View itemView)
        {
            super(itemView);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.subRecyclerView);
            textViewBodyPart = (TextView) itemView.findViewById(R.id.listBodyPart);
            subItem = (View) itemView.findViewById(R.id.workoutNameContainer);

        }

        private void bind(UserWorkoutCategory userWorkoutCategory) {
            boolean expanded = userWorkoutCategory.isExpanded();

            recyclerView.setVisibility(expanded ? View.VISIBLE : View.VISIBLE);
        }
    }
}

