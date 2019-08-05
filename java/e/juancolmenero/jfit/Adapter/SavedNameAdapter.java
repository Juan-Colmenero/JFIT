package e.juancolmenero.jfit.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import e.juancolmenero.jfit.Activities.SavedWorkoutActivity;
import e.juancolmenero.jfit.Models.SavedNames;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class SavedNameAdapter extends RecyclerView.Adapter<SavedNameAdapter.WorkoutViewHolder> {

    Context context;
    ArrayList<SavedNames> arrayList;

    private int user_id;
    private TextView mName;

    public SavedNameAdapter(Context context, ArrayList<SavedNames> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public SavedNameAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_names, parent, false);
        return new SavedNameAdapter.WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedNameAdapter.WorkoutViewHolder holder, final int position) {

        final SavedNames savedNames = arrayList.get(position);
        final String name;

        //int KEY_ID = savedNames.getUser_id();
        final int name_id = savedNames.getName_id();
        user_id = savedNames.getUser_id();
        name = savedNames.getName();

        mName = holder.textViewName;

        holder.textViewName.setText(name);
        holder.workoutNameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,savedNames.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SavedWorkoutActivity.class);
                intent.putExtra("name_id",name_id);
                context.startActivity(intent);

            }
        });

        holder.opions_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkoutDialog(name_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        LinearLayout workoutNameContainer;
        ImageView opions_icon;


        public WorkoutViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.workout_lbl);
            workoutNameContainer = itemView.findViewById(R.id.workoutNameContainer);
            opions_icon = itemView.findViewById(R.id.options_icon);
            //workout_holder_color = (LinearLayout) itemView.findViewById(R.id.workoutNameContainer);

        }
    }

    private void showWorkoutDialog(final int name_id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("What would you like to do?")
                .setItems(R.array.saved_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //Add to Calendar
                            //sendToCalendarDialog(bodyPart, name, sets, reps);
                        }else if (which == 1){
                            //Edit Name
                            editExerciseDialog(name_id);
                        }else if(which == 2){
                            //Delete from database and UI.
                            //deleteSingleWorkout(name_id, workout_id);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //TODO: FINISH CREATING DIALOG!! 07/31/2019
    private void editExerciseDialog(final int name_id){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_update_saved_name, null);
        TextView cancel_tv, add_tv;
        final EditText name_et;

        name_et = promptsView.findViewById(R.id.name_et);
        cancel_tv = promptsView.findViewById(R.id.cancel_tv);
        add_tv = promptsView.findViewById(R.id.add_tv);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        add_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PUT CODE TO SEND UPDATE
                String name = name_et.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    name_et.setError("Please enter name");
                    name_et.requestFocus();
                    return;
                }
                updateName(name, name_id);
                notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // show it
        alertDialog.show();
    }

    private void updateName(final String name, final  int name_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_SAVED_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                mName.setText(name);
                            } else {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", String.valueOf(name));
                params.put("name_id", String.valueOf(name_id));
                params.put("user_id", String.valueOf(user_id));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
