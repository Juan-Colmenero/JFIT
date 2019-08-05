package e.juancolmenero.jfit.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import e.juancolmenero.jfit.Activities.SavedNamesActivity;
import e.juancolmenero.jfit.Models.SavedNames;
import e.juancolmenero.jfit.Models.UserWorkout;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class FavoriteExerciseSubAdapter extends RecyclerView.Adapter<FavoriteExerciseSubAdapter.SubCategoryViewHolder>{
    Context context;
    ArrayList<UserWorkoutSubcategory> arrayList;
    private String mDate;
    private int KEY_ID;
    private String bodyPart, date;
    private int sets, reps, name_id;
    int [] list_ids;
    private String selected_name;

    public FavoriteExerciseSubAdapter(Context context, ArrayList<UserWorkoutSubcategory> arrayList, String bodyPart, String mDate, int KEY_ID)
    {
        this.KEY_ID = KEY_ID;
        this.context = context;
        this.arrayList = arrayList;
        this.mDate = mDate;
        this.bodyPart = bodyPart;
    }

    @Override
    public FavoriteExerciseSubAdapter.SubCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_exercise_subcategory,parent,false);
        return new FavoriteExerciseSubAdapter.SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteExerciseSubAdapter.SubCategoryViewHolder holder, int position) {
        final UserWorkoutSubcategory userWorkoutSubcategory = arrayList.get(position);
        final int exercise_id = userWorkoutSubcategory.getExerciseID();
        holder.textViewWorkoutName.setText(userWorkoutSubcategory.getName());
        final TextView name_tv = holder.textViewWorkoutName;


        if(position %2 == 1) {
            holder.subCategoryHolder.setBackgroundColor(Color.parseColor("#F7F9FB"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {
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
                showWorkoutDialog(bodyPart ,userWorkoutSubcategory.getName(), exercise_id, name_tv);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWorkoutName;
        LinearLayout subCategoryHolder;
        ImageView delete_icon;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);

            textViewWorkoutName = itemView.findViewById(R.id.workoutName);
            subCategoryHolder = itemView.findViewById(R.id.subCategoryHolder);
            delete_icon = itemView.findViewById(R.id.delete_icon);
        }
    }

    private void showWorkoutDialog(final String bodyPart, final String name, final int exercise_id, final TextView name_tv){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("What would you like to do?")
                .setItems(R.array.favorites_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //Add to Calendar
                            sendToCalendarDialog(bodyPart, name);
                        }else if (which == 1){
                            //Add to Saved Workouts
                            saveWorkoutDialog(name);
                        }else if (which == 2){
                            //Edit Name
                            editExerciseDialog(exercise_id, name_tv);
                        }else if(which == 3){
                            //Delete from database and UI
                            //TODO: update UI after deleting
                            deleteSingleWorkout(bodyPart, name);
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

    private void editExerciseDialog(final int exercise_id, final TextView name_tv){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_update_saved_name, null);
        TextView cancel_tv, add_tv, title;
        final EditText name_et;

        title = promptsView.findViewById(R.id.title);
        name_et = promptsView.findViewById(R.id.name_et);
        cancel_tv = promptsView.findViewById(R.id.cancel_tv);
        add_tv = promptsView.findViewById(R.id.add_tv);

        title.setText("Edit your favorite exercise name");

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
                updateName(name, exercise_id, name_tv);
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
    //TODO: YOUR FINISHED BUT UPDATE UI
    private void updateName(final String name, final int exercise_id, final TextView name_tv){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_FAVORITE_EXERCISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                name_tv.setText(name);
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
                params.put("id", String.valueOf(exercise_id));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void deleteSingleWorkout(final String body_part, final String workoutName){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_FAVORITE_EXERCISE,
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
                params.put("body_part", String.valueOf(body_part));
                params.put("workout_name", String.valueOf(workoutName));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void sendToCalendarDialog(final String bodyPart, final String name){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_workout, null);

        final String workoutBodyType, workoutName;

        // Create a calendar object and set year and month
        final Calendar mycal = new GregorianCalendar();
        final SimpleDateFormat dateFormatQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatQuery.setTimeZone(mycal.getTimeZone());

        workoutBodyType = bodyPart;
        workoutName = name;
        final EditText workoutSet_et = promptsView.findViewById(R.id.popup_set_et);
        final EditText workoutRep_et = promptsView.findViewById(R.id.popup_rep_et);
        final DatePicker datePicker = promptsView.findViewById(R.id.date_picker);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sets = Integer.parseInt(workoutSet_et.getText().toString());
                                reps = Integer.parseInt(workoutRep_et.getText().toString());
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year = datePicker.getYear();
                                mycal.set(year, month, day);
                                date = dateFormatQuery.format(mycal.getTime());

                                UserWorkout userWorkout = new UserWorkout(date, workoutBodyType, workoutName, sets, reps);

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

    private void sendWorkout(UserWorkout userWorkout){
        final String date, body, workoutName;
        final int sets, reps;
        final int userID;

        date = userWorkout.getDate();
        body = userWorkout.getBodyPart();
        workoutName = userWorkout.getWorkoutName();
        sets = userWorkout.getSets();
        reps = userWorkout.getReps();
        userID = KEY_ID;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEND_WORKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("user_id", String.valueOf(userID));
                return params;
            }

        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void saveWorkoutDialog(final String workout_name){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_add_favorite, null);
        TextView cancel_tv, add_tv;
        final EditText sets_et, reps_et;

        sets_et = promptsView.findViewById(R.id.sets_et);
        reps_et = promptsView.findViewById(R.id.reps_et);

        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.list_spinner);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_SAVED_NAMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                ArrayList<SavedNames> arrayListSavedNames = new ArrayList<>();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONArray names_list = userJson.getJSONArray("list_of_names");
                                for(int i = 0; i<names_list.length(); i++){
                                    JSONObject objectName = names_list.getJSONObject(i);
                                    SavedNames savedNames = new SavedNames();
                                    savedNames.setName_id(objectName.getInt("id"));
                                    savedNames.setName(objectName.getString("workout_name"));
                                    savedNames.setUser_id(KEY_ID);
                                    arrayListSavedNames.add(savedNames);
                                }

                                String [] names = new String[arrayListSavedNames.size()];
                                int [] id = new int [arrayListSavedNames.size()];

                                for(int x = 0; x < arrayListSavedNames.size(); x ++){
                                    names[x] = arrayListSavedNames.get(x).getName();
                                    id[x] = arrayListSavedNames.get(x).getName_id();
                                }

                                list_ids = id;
                                // Create an ArrayAdapter using the string array and a default spinner layout
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, names);
                                // Specify the layout to use when the list of choices appears
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // Apply the adapter to the spinner
                                spinner.setAdapter(adapter);

                            } else {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TOAST A MESSAGE OR LOG TO SEE IF THERE IS AN ERROR
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_name = spinner.getItemAtPosition(position).toString();
                name_id = list_ids[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                String sets = sets_et.getText().toString();
                String reps = reps_et.getText().toString();
                sendToSavedWorkouts(name_id, workout_name, sets, reps);
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

    public void sendToSavedWorkouts(final int name_id, final String workout_name,
                                    final String sets, final String reps){
        System.out.println(name_id);
        System.out.println(workout_name);
        System.out.println(sets);
        System.out.println(reps);
        System.out.println(KEY_ID);
        System.out.println(bodyPart);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_FROM_FAV_TO_SAV,
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
                params.put("name_id", String.valueOf(name_id));
                params.put("body_part", String.valueOf(bodyPart));
                params.put("workout_name", String.valueOf(workout_name));
                params.put("sets", String.valueOf(sets));
                params.put("reps", String.valueOf(reps));
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}
