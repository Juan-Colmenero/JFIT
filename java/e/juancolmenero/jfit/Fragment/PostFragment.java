package e.juancolmenero.jfit.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.PostWorkoutAdapter;
import e.juancolmenero.jfit.Activities.CreatePostActivity;
import e.juancolmenero.jfit.Models.UserWorkout;
import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.Activities.PickBodyActivity;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

/*
* TODO: CREATE CUSTOMDIALOG POPUP TO CREATE WORKOUT
* XML is done already, need java code, copy past from previous app
* TODO: CREATE RECYCLERVIEW TO DISPLAY CREATED WORKOUT
* Copy code from workout app, you will need to eliminate the nested recycler view open
* and create a popup per body part workout to display full workout.
* TODO: ADD TWO BUTTONS (CREATE WORKOUT, CREATE_POST)
* You will have to reate models to store data and communicate with database.
* */




public class PostFragment extends Fragment {

    //RecyclerView verticalRecyclerView;
    private RecyclerView mPostRecyclerView;

    //VerticalRecyclerViewAdapter adapter;
    private PostWorkoutAdapter mAdapter;
    private Set<String> setOfKeySet;
    private ArrayList<UserWorkoutCategory> arrayListUserWorkout;

    private CalendarView calendarView;
    private Spinner spinner;
    private Button showPopupBtn;
    private String date, workoutName, workoutBodyType;
    private int KEY_ID;
    private int sets, reps;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    private RequestQueue requestQueue;  // Assume this exists.

    //Added for sub_fabs
    private boolean fabExpanded = false;
    private LinearLayout layoutFabSave;
    private TextView add_workout_menu;
    private TextView add_post_menu;
    private  View view;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        view = rootView;

        // Create a calendar object and set year and month
        final Calendar mycal = new GregorianCalendar();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        final SimpleDateFormat dateFormatQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(mycal.getTimeZone());
        dateFormatQuery.setTimeZone(mycal.getTimeZone());
        date = dateFormatQuery.format(mycal.getTime());

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListUserWorkout = new ArrayList<>();

        mPostRecyclerView = (RecyclerView) rootView.findViewById(R.id.post_recylcer_view);
        mPostRecyclerView.setHasFixedSize(true);

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        mAdapter = new PostWorkoutAdapter(getActivity(), arrayListUserWorkout);

        mPostRecyclerView.setAdapter(mAdapter);

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        //find views to attach
        calendarView = (CalendarView) rootView.findViewById(R.id.post_calendarView);

        showPopupBtn = (Button) rootView.findViewById(R.id.post_workout_dialog);

        getWorkout(dateFormatQuery.format(mycal.getTime()));


        //TODO: No need to use if Statments here, use the dateFormatter instead for accuracy as you did in Dialogs DatePicker
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final SimpleDateFormat jFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

                mycal.set(year, month, dayOfMonth);
                System.out.println(dateFormat.format(mycal.getTime()));

                String monthHolder, dayHolder;
                if (month < 10){
                    monthHolder = "0" + (month + 1);
                } else {monthHolder = Integer.toString(month); }
                if (dayOfMonth < 10){
                    dayHolder = "0" + (dayOfMonth);
                }else {dayHolder = Integer.toString(dayOfMonth); }

                date = year + "-" + monthHolder + "-" + dayHolder;
                System.out.println(date);
                String storeDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                //date = (month + 1) + "/" + dayOfMonth + "/" + year;


                getWorkout(date);
            }
        });

        collapsingToolbar = rootView.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded);

        //This is the most important when you are putting custom textview in CollapsingToolbar
        //collapsingToolbar.setTitle(" ");

        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    collapsingToolbar.setTitle(dateFormat.format(mycal.getTime()));
                } else if (verticalOffset == 0) {
                    // Expanded
                    collapsingToolbar.setTitle(" ");
                } else {
                    // Somewhere in between
                }
            }
        });

        layoutFabSave = (LinearLayout) rootView.findViewById(R.id.layoutFabSave);
        add_workout_menu = rootView.findViewById(R.id.add_workout);
        add_post_menu = rootView.findViewById(R.id.add_post);

        FloatingActionButton fab = rootView.findViewById(R.id.post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }

                //Original fab
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (TextUtils.isEmpty(date)) {
                    String badDate = "Select Date";
                    Toast.makeText(getContext(), badDate, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), PickBodyActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);*/
                //showWorkoutDialog();
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        //TODO: WILL REMOVE WITH FLOATING BAR
        /*showPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(date)) {
                    String badDate = "Select Date";
                    Toast.makeText(getContext(), badDate, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), PickBodyActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
                //showWorkoutDialog();
            }
        });*/

    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabSave.setVisibility(View.INVISIBLE);

        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabSave.setVisibility(View.VISIBLE);
        add_workout_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Original fab
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (TextUtils.isEmpty(date)) {
                    String badDate = "Select Date";
                    Toast.makeText(getContext(), badDate, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), PickBodyActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        add_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Original fab
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (TextUtils.isEmpty(date)) {
                    String badDate = "Select Date";
                    Toast.makeText(getContext(), badDate, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        //Change settings icon to 'X' icon
        fabExpanded = true;
    }



    /*private void showWorkoutDialog(){
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.popup_workout, null);

        final EditText workoutName_et = (EditText) promptsView.findViewById(R.id.workoutName_et);
        final EditText workoutSet_et = (EditText) promptsView.findViewById(R.id.popup_set_et);
        final EditText workoutRep_et = (EditText) promptsView.findViewById(R.id.popup_rep_et);
        //final TextView workoutDate_tv = (TextView) promptsView.findViewById(R.id.popup_date_selected);
        //workoutDate_tv.setText(date);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                //spinner = (Spinner) promptsView.findViewById(R.id.body_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.body_part_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        //Get value amd pass to string
                        workoutBodyType = spinner.getItemAtPosition(i).toString();
                    }
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        spinner.setPrompt("Select Workout Type..");
                    }
                });

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("ADD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        workoutName = workoutName_et.getText().toString();
                                        sets = Integer.parseInt(workoutSet_et.getText().toString());
                                        reps = Integer.parseInt(workoutRep_et.getText().toString());

                                        UserWorkout userWorkout = new UserWorkout(date, workoutBodyType, workoutName,
                                                sets, reps);

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
    }*/

    //TODO: MAY NO LONGER NEED, THIS WAS USED WITH SHOWORKOUTDIALOG() WHICH IS NO LONGER NEEDED.
    // CHECK IF YOU NEED TO DELETE. SAVE CODE FOR REFERENCES
    public void sendWorkout(UserWorkout userWorkout){
        final String date, body, workoutName;
        final int sets, reps;
        final int userID;

        removeAllItems();

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
                            //Log.d("TAG", obj.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONObject baseObject = userJson.getJSONObject("workout");


                                Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<String, ArrayList<ArrayList<String>>>();
                                Iterator<String> iter = baseObject.keys();

                                while (iter.hasNext()) {
                                    String key = iter.next();

                                    JSONObject workoutArray = baseObject.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");

                                    ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();

                                    String name, sets, reps;
                                     for(int i = 0; i < actualWorkout.length(); i++)
                                     {
                                         ArrayList<String> list=new ArrayList<String>();//Creating arraylist
                                         JSONObject obj1 = actualWorkout.getJSONObject(i);
                                         name = obj1.getString("name");
                                         sets = Integer.toString(obj1.getInt("sets"));
                                         reps = Integer.toString(obj1.getInt("reps"));
                                         list.add(name);
                                         list.add(sets);
                                         list.add(reps);
                                         myList.add(list);
                                         workout_container.put(key, myList);
                                     }
                                }

                                //SET DATA AND POPULATE ONTO RECYCLER VIEW
                                setData(workout_container);

                            } else {
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                System.out.println(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("user_id", String.valueOf(userID));
                return params;
            }

        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    //TODO get workoutJSON and extract it to display on screen!
    private void setData(Map<String, ArrayList<ArrayList<String>>> workout_container)
    {

        setOfKeySet = workout_container.keySet();

        for (String key : setOfKeySet) {
            UserWorkoutCategory workoutListCategoryModel = new UserWorkoutCategory();
            workoutListCategoryModel.setDate(date);
            workoutListCategoryModel.setBodyPart(key);
            workoutListCategoryModel.setKEY_ID(KEY_ID);
            ArrayList<UserWorkoutSubcategory> arrayUserSubcategory = new ArrayList<>();

            for (ArrayList<String> workoutName : workout_container.get(key)) {
                //System.out.println("Workout - " + workoutName);
                UserWorkoutSubcategory userWorkoutSubcategory = new UserWorkoutSubcategory();
                userWorkoutSubcategory.setName(workoutName.get(0));
                userWorkoutSubcategory.setSets(workoutName.get(1));
                userWorkoutSubcategory.setReps(workoutName.get(2));
                arrayUserSubcategory.add(userWorkoutSubcategory);
            }

            workoutListCategoryModel.setArrayList(arrayUserSubcategory);
            arrayListUserWorkout.add(workoutListCategoryModel);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void removeAllItems() {
        arrayListUserWorkout.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void getWorkout(String date){

        final String mDate = date;

        removeAllItems();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_WORKOUT,
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
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //FIX API FOR FETCH, MAKE (OBJECT) on $RESULT if THERE IS DATA LIKE FETCHING user_post
                                JSONObject baseObject = userJson.getJSONObject("workout");

                                Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<String, ArrayList<ArrayList<String>>>();
                                Iterator<String> iter = baseObject.keys();

                                while (iter.hasNext()) {
                                    String key = iter.next();

                                    JSONObject workoutArray = baseObject.getJSONObject(key);
                                    JSONArray actualWorkout = workoutArray.getJSONArray("workout");

                                    ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();

                                    String name, sets, reps;
                                    for(int i = 0; i < actualWorkout.length(); i++)
                                    {
                                        ArrayList<String> list=new ArrayList<String>();//Creating arraylist
                                        JSONObject obj1 = actualWorkout.getJSONObject(i);
                                        name = obj1.getString("name");
                                        sets = Integer.toString(obj1.getInt("sets"));
                                        reps = Integer.toString(obj1.getInt("reps"));
                                        list.add(name);
                                        list.add(sets);
                                        list.add(reps);
                                        myList.add(list);
                                        workout_container.put(key, myList);
                                    }
                                }


                                //SET DATA AND POPULATE ONTO RECYCLER VIEW
                                setData(workout_container);

                            } else {
                                Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                System.out.println(obj.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e);
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
                params.put("user_id", String.valueOf(KEY_ID));
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
