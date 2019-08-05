package e.juancolmenero.jfit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;

public class UserSubWorkoutAdapter extends RecyclerView.Adapter<UserSubWorkoutAdapter.SubCategoryViewHolder>{
    Context context;
    ArrayList<UserWorkoutSubcategory> arrayList;
    String mDate;
    private int KEY_ID;
    String bodyPart;

    public UserSubWorkoutAdapter(Context context, ArrayList<UserWorkoutSubcategory> arrayList, String bodyPart, String mDate, int KEY_ID)
    {
        this.KEY_ID = KEY_ID;
        this.context = context;
        this.arrayList = arrayList;
        this.mDate = mDate;
        this.bodyPart = bodyPart;
    }

    @Override
    public UserSubWorkoutAdapter.SubCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_workout_subcategory,parent,false);
        return new UserSubWorkoutAdapter.SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSubWorkoutAdapter.SubCategoryViewHolder holder, int position) {
        final UserWorkoutSubcategory userWorkoutSubcategory = arrayList.get(position);
        holder.textViewWorkoutName.setText(userWorkoutSubcategory.getName());
        holder.textViewSets.setText(userWorkoutSubcategory.getSets());
        holder.textViewReps.setText(userWorkoutSubcategory.getReps());

        if(position %2 == 1)
        {
            holder.subCategoryHolder.setBackgroundColor(Color.parseColor("#F7F9FB"));

            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.subCategoryHolder.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,userWorkoutSubcategory.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.add_icon.setImageResource(R.drawable.green_add_icon);
        holder.add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDate;
                Toast.makeText(context,"ADD WORKOUT DUMBY",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWorkoutName;
        TextView textViewSets;
        TextView textViewReps;
        LinearLayout subCategoryHolder;
        ImageView add_icon;
        RecyclerView recyclerView;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);

            textViewWorkoutName = (TextView) itemView.findViewById(R.id.workoutName);
            textViewSets = (TextView) itemView.findViewById(R.id.sets);
            textViewReps = (TextView) itemView.findViewById(R.id.reps);
            subCategoryHolder = itemView.findViewById(R.id.subCategoryHolder);
            add_icon = (ImageView) itemView.findViewById(R.id.delete_icon);
        }

        private void bind(UserWorkoutCategory userWorkoutCategory) {
            boolean expanded = userWorkoutCategory.isExpanded();

            recyclerView.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }

}