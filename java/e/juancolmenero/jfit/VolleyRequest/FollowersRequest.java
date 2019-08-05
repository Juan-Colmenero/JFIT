package e.juancolmenero.jfit.VolleyRequest;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class FollowersRequest {
    private int KEY_ID;
    private Activity context;
    private int FOLLOWER_ID;

    public FollowersRequest(Activity context, int KEY_ID){
        this.context = context;
        this.KEY_ID = KEY_ID;
    }

    public int getFOLOWER_ID(){
        return FOLLOWER_ID;
    }

    public void setFOLOWER_ID(int FOLLOWER_ID) {
        this.FOLLOWER_ID = FOLLOWER_ID;
    }

    public void getFollowers(final VolleyCallback callback){
        final int userID;

        userID = KEY_ID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_FOLLOWERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                params.put("user_id", String.valueOf(userID));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void getFollowing(final VolleyCallback callback){
        final int userID;

        userID = KEY_ID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_FOLLOWING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                params.put("user_id", String.valueOf(userID));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void follow(final VolleyCallback callback){
        final int USER_ID;
        final int FOLLOWER_ID;

        USER_ID = KEY_ID;
        FOLLOWER_ID = this.FOLLOWER_ID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FOLLOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                params.put("user_id", String.valueOf(USER_ID));
                params.put("follower_id", String.valueOf(FOLLOWER_ID));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void unFollow(final VolleyCallback callback){
        final int USER_ID;
        final int FOLLOWER_ID;

        USER_ID = KEY_ID;
        FOLLOWER_ID = this.FOLLOWER_ID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UNFOLLOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                params.put("user_id", String.valueOf(USER_ID));
                params.put("follower_id", String.valueOf(FOLLOWER_ID));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

}
