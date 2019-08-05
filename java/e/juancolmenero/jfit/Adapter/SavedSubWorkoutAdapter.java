package e.juancolmenero.jfit.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import e.juancolmenero.jfit.Models.SaveExerciseModel;
import e.juancolmenero.jfit.Models.UserWorkout;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class SavedSubWorkoutAdapter extends RecyclerView.Adapter<SavedSubWorkoutAdapter.SubCategoryViewHolder>{
    private Context context;
    private ArrayList<UserWorkoutSubcategory> arrayList;
    private int KEY_ID;
    private int name_id, user_id;
    private String mDate, bodyPart;

    public SavedSubWorkoutAdapter(Context context, ArrayList<UserWorkoutSubcategory> arrayList, int workoutName_id, String bodyPart, int KEY_ID)
    {
        this.KEY_ID = KEY_ID;
        this.user_id = KEY_ID;
        this.context = context;
        this.arrayList = arrayList;
        this.name_id = workoutName_id;
        this.bodyPart = bodyPart;
    }

    @Override
    public SavedSubWorkoutAdapter.SubCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_sub_workout,parent,false);
        return new SavedSubWorkoutAdapter.SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedSubWorkoutAdapter.SubCategoryViewHolder holder, int position) {
        final UserWorkoutSubcategory userWorkoutSubcategory = arrayList.get(position);
        final TextView name_tv, sets_tv, reps_tv;
        final LinearLayout subHolder;
        holder.textViewWorkoutName.setText(userWorkoutSubcategory.getName());
        holder.textViewSets.setText(userWorkoutSubcategory.getSets());
        holder.textViewReps.setText(userWorkoutSubcategory.getReps());
        name_tv = holder.textViewWorkoutName;
        sets_tv = holder.textViewSets;
        reps_tv = holder.textViewReps;
        subHolder = holder.subCategoryHolder;

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

        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveExerciseModel saveExerciseModel = new SaveExerciseModel(userWorkoutSubcategory.getExerciseID(),
                        userWorkoutSubcategory.getName(), userWorkoutSubcategory.getSets(), userWorkoutSubcategory.getReps(),
                        name_tv, sets_tv, reps_tv, subHolder);
                showWorkoutDialog(bodyPart, saveExerciseModel);
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

            textViewWorkoutName = (TextView) itemView.findViewById(R.id.workoutName);
            textViewSets = (TextView) itemView.findViewById(R.id.sets);
            textViewReps = (TextView) itemView.findViewById(R.id.reps);
            subCategoryHolder = itemView.findViewById(R.id.subCategoryHolder);
            delete_icon = (ImageView) itemView.findViewById(R.id.delete_icon);
        }

    }

    private void showWorkoutDialog(final String bodyPart, final SaveExerciseModel saveExerciseModel){
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
                            sendToCalendarDialog(bodyPart, saveExerciseModel.getWorkoutName(), saveExerciseModel.getSets(), saveExerciseModel.getReps());
                        }else if (which == 1){
                            //Edit Name
                            editExerciseDialog(saveExerciseModel);
                        }else if(which == 2){
                            //Delete from database and UI.
                            deleteSingleWorkout(name_id, saveExerciseModel);
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

    private void sendToCalendarDialog(final String bodyPart, final String name, final String sets, final String reps){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_send_saved, null);

        final String workoutBodyType, workoutName;
        final int mSets, mReps;
        mSets = Integer.parseInt(sets);
        mReps = Integer.parseInt(reps);
        workoutBodyType = bodyPart;
        workoutName = name;

        // Create a calendar object and set year and month
        final Calendar mycal = new GregorianCalendar();
        final SimpleDateFormat dateFormatQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatQuery.setTimeZone(mycal.getTimeZone());
        final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.date_picker);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year = datePicker.getYear();
                                mycal.set(year, month, day);
                                mDate = dateFormatQuery.format(mycal.getTime());

                                UserWorkout userWorkout = new UserWorkout(mDate, workoutBodyType, workoutName, mSets, mReps);
                                sendWorkout(userWorkout);
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

    private void deleteSingleWorkout(final int name_id, final SaveExerciseModel saveExerciseModel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_SAVED_EXERCISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                saveExerciseModel.getHolder().setVisibility(View.GONE);
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
                params.put("saved_name_id", String.valueOf(name_id));
                params.put("saved_workout_id", String.valueOf(saveExerciseModel.getWorkout_id()));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void editExerciseDialog(final SaveExerciseModel saveExerciseModel){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_update_saved, null);

        TextView cancel_tv, add_tv;
        final EditText name_et, sets_et, reps_et;

        name_et = promptsView.findViewById(R.id.name_et);
        sets_et = promptsView.findViewById(R.id.set_et);
        reps_et = promptsView.findViewById(R.id.rep_et);
        cancel_tv = promptsView.findViewById(R.id.cancel_tv);
        add_tv = promptsView.findViewById(R.id.add_tv);

        name_et.setHint(saveExerciseModel.getWorkoutName());
        sets_et.setHint(saveExerciseModel.getSets());
        reps_et.setHint(saveExerciseModel.getReps());

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
                String n = name_et.getText().toString().trim();
                if (TextUtils.isEmpty(n)) {
                    name_et.setError("Please enter name");
                    name_et.requestFocus();
                    return;
                }

                String s = sets_et.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    sets_et.setError("Please enter sets");
                    sets_et.requestFocus();
                    return;
                }

                String r = reps_et.getText().toString().trim();
                if (TextUtils.isEmpty(r)) {
                    reps_et.setError("Please enter reps");
                    reps_et.requestFocus();
                    return;
                }

                updateExercise(saveExerciseModel, n, s, r);
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

    private void updateExercise(final SaveExerciseModel saveExerciseModel, final String name, final String sets, final String reps){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_SAVED_EXERCISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                saveExerciseModel.getName_tv().setText(name);
                                saveExerciseModel.getSets_tv().setText(sets);
                                saveExerciseModel.getReps_tv().setText(reps);
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
                params.put("user_id", String.valueOf(user_id));
                params.put("name_id", String.valueOf(name_id));
                params.put("workout_id", String.valueOf(saveExerciseModel.getWorkout_id()));
                params.put("workout_name", String.valueOf(name));
                params.put("sets", String.valueOf(sets));
                params.put("reps", String.valueOf(reps));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void sendWorkout(UserWorkout userWorkout){
        final String date, body, workoutName;
        final int sets, reps;

        date = userWorkout.getDate();
        body = userWorkout.getBodyPart();
        workoutName = userWorkout.getWorkoutName();
        sets = userWorkout.getSets();
        reps = userWorkout.getReps();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEND_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("body_part", body);
                params.put("workout_name", workoutName);
                params.put("sets", String.valueOf(sets));
                params.put("reps", String.valueOf(reps));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }

        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
