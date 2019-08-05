package e.juancolmenero.jfit.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class FavoriteExerciseAdapter extends RecyclerView.Adapter<FavoriteExerciseAdapter.WorkoutViewHolder> {

    Context context;
    ArrayList<UserWorkoutCategory> arrayList;
    private int USER_ID;
    private String body_part;
    private String mDate;

    public FavoriteExerciseAdapter(Context context, ArrayList<UserWorkoutCategory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public FavoriteExerciseAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_exercise_category, parent, false);
        return new FavoriteExerciseAdapter.WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteExerciseAdapter.WorkoutViewHolder holder, final int position) {

        final UserWorkoutCategory userWorkoutCategory = arrayList.get(position);

        int KEY_ID = userWorkoutCategory.getKEY_ID();
        USER_ID = userWorkoutCategory.getKEY_ID();
        mDate = userWorkoutCategory.getDate();
        body_part = userWorkoutCategory.getBodyPart();

        String title = userWorkoutCategory.getBodyPart();

        //USE POSITION TO DISPLAY CORRECT COLORS< CURRENTLY YOUR ONLY USING STRING OF BODY
        switch (userWorkoutCategory.getBodyPart()) {
            case "Chest":{
                holder.color_category.setBackgroundColor(Color.parseColor("#F84747"));}
                break;

            case "Legs":{
                holder.color_category.setBackgroundColor(Color.parseColor("#47F87D"));}
                break;

            case "Back":{
                holder.color_category.setBackgroundColor(Color.parseColor("#477DF8"));}
                break;

            case "Biceps":{
                holder.color_category.setBackgroundColor(Color.parseColor("#FAC95B"));}
                break;
            case "Shoulders":{
                holder.color_category.setBackgroundColor(Color.parseColor("#7747F8"));}
                break;

            case "Triceps":{
                holder.color_category.setBackgroundColor(Color.parseColor("#FA9E5B"));}
                break;
            case "Abs":{
                holder.color_category.setBackgroundColor(Color.parseColor("#F84784"));}
                break;

            case "Other":{
                holder.color_category.setBackgroundColor(Color.parseColor("#47C2F8"));}
                break;

            default:
                break;


        }

        holder.bind(userWorkoutCategory);
        ArrayList<UserWorkoutSubcategory> singleItem = userWorkoutCategory.getArrayList();

        holder.textViewBodyPart.setText(title);
        FavoriteExerciseSubAdapter favoriteExerciseSubAdapter = new FavoriteExerciseSubAdapter(context, singleItem, userWorkoutCategory.getBodyPart(), userWorkoutCategory.getDate(), KEY_ID);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        holder.recyclerView.setAdapter(favoriteExerciseSubAdapter);


        holder.subItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showWorkoutDialog(userWorkoutCategory.getBodyPart());
                return true;
            }
        });
        holder.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //THIS IS TO LAUNCH THE OTHER METHOD TO ANOTHER SCREEN TO VIEW WORKOUT
                /*Intent intent = new Intent(context, DisplayUserWorkoutActivity.class);
                intent.putExtra("date", userWorkoutCategory.getDate());

                    context.startActivity(intent);*/

                Toast.makeText(context, userWorkoutCategory.getBodyPart(), Toast.LENGTH_SHORT).show();
                boolean expanded = userWorkoutCategory.isExpanded();

                userWorkoutCategory.setExpanded(!expanded);
                if (!expanded) {
                    holder.subItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                notifyItemChanged(position);

                switch (userWorkoutCategory.getBodyPart()) {
                    case "Chest":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#F47777"));}
                        break;

                    case "Legs":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#A8FFC3"));}
                        break;

                    case "Back":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#83A9FF"));}
                        break;

                    case "Biceps":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#FFDD90"));}
                        break;
                    case "Shoulders":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#926BFC"));}
                        break;

                    case "Triceps":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#FA9E5B"));}
                        break;
                    case "Abs":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#FF9DBF"));}
                        break;

                    case "Other":{
                        holder.subItem.setBackgroundColor(Color.parseColor("#AAE0F8"));}
                        break;
                    default:
                        break;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textViewBodyPart;
        View subItem;
        LinearLayout color_category, workout_holder_color;


        public WorkoutViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.subRecyclerView);
            textViewBodyPart = itemView.findViewById(R.id.listBodyPart);
            subItem = itemView.findViewById(R.id.workoutNameContainer);
            color_category = itemView.findViewById(R.id.color_identifier);
            //workout_holder_color = (LinearLayout) itemView.findViewById(R.id.workoutNameContainer);

        }

        private void bind(UserWorkoutCategory userWorkoutCategory) {
            boolean expanded = userWorkoutCategory.isExpanded();

            recyclerView.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }

    private void showWorkoutDialog(String bodyPart) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setMessage("Delete all of " + bodyPart + " workout?")
                .setPositiveButton("AGREE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteBodyWorkout();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void deleteBodyWorkout() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_BODY_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
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
                params.put("user_id", String.valueOf(USER_ID));
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}

