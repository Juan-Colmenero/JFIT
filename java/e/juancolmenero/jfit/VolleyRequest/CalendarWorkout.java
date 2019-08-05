package e.juancolmenero.jfit.VolleyRequest;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarWorkout {
    private static final String json_url = "http://www.juancolmenero.com/login_configuration.php";
    private String date;
    private Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<>();
    private RequestQueue requestQueue;  // Assume this exists.
    private int KEY_ID;
    private Activity context;

    public CalendarWorkout (Activity context, String date, int KEY_ID){
        this.date = date;
        this.context = context;
        this.KEY_ID = KEY_ID;
    }

    public Map<String, ArrayList<ArrayList<String>>> getWorkout(){
        final int userID;

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
                params.put("date", date);
                params.put("user_id", String.valueOf(userID));
                return params;
            }
        };

        //MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        return workout_container;
        //TODO: SEND WORKOUT to UI TO UPDATE IN FOR LOOP
    }


}
