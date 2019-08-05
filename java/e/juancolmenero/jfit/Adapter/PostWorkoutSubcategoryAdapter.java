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
import java.util.Iterator;
import java.util.Map;

import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class PostWorkoutSubcategoryAdapter extends RecyclerView.Adapter<PostWorkoutSubcategoryAdapter.SubCategoryViewHolder>{
    Context context;
    ArrayList<UserWorkoutSubcategory> arrayList;
    private String mDate;
    private int KEY_ID;
    private String bodyPart;

    public PostWorkoutSubcategoryAdapter(Context context, ArrayList<UserWorkoutSubcategory> arrayList, String bodyPart, String mDate, int KEY_ID)
    {
        this.KEY_ID = KEY_ID;
        this.context = context;
        this.arrayList = arrayList;
        this.mDate = mDate;
        this.bodyPart = bodyPart;
    }

    @Override
    public SubCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_workout_subcategory,parent,false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
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

        holder.delete_icon.setImageResource(R.drawable.ic_delete_red_24dp);
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDate;
                deleteSingleWorkout(date, bodyPart ,userWorkoutSubcategory.getName(), userWorkoutSubcategory.getSets(), userWorkoutSubcategory.getReps());
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
        ImageView delete_icon;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);

            textViewWorkoutName = itemView.findViewById(R.id.workoutName);
            textViewSets = itemView.findViewById(R.id.sets);
            textViewReps = itemView.findViewById(R.id.reps);
            subCategoryHolder = itemView.findViewById(R.id.subCategoryHolder);
            delete_icon = itemView.findViewById(R.id.delete_icon);
        }

    }

    private void deleteSingleWorkout(final String mDate, final String body_part, final String workoutName, final String sets,  final String reps){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_ONE_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //Log.d("TAG", obj.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                System.out.println(obj.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TOAST A MESSAGE OR LOG TO SEE IF THERE IS AN ERROR
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", mDate);
                params.put("body_part", String.valueOf(body_part));
                params.put("workout_name", String.valueOf(workoutName));
                params.put("sets", String.valueOf(sets));
                params.put("reps", String.valueOf(reps));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}