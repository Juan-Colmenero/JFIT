package e.juancolmenero.jfit.VolleyRequest;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostInteractionRequest {
    private static final String json_url = "http://www.juancolmenero.com/login_configuration.php";
    Map<String, ArrayList<ArrayList<String>>> workout_container = new HashMap<String, ArrayList<ArrayList<String>>>();
    private RequestQueue requestQueue;  // Assume this exists.
    private int KEY_ID;
    private Activity context;
    private JSONObject baseObject;
    private String mResponse;
    private int post_id;
    private int follower_id;

    public PostInteractionRequest(Activity context, int KEY_ID){
        this.context = context;
        this.KEY_ID = KEY_ID;
    }

    public void setPOST_ID(int post_id) {
        this.post_id = post_id;
    }

    public void setFOLOWER_ID(int follower_id) {
        this.follower_id = follower_id;
    }

    public void getFollowers(final FollowersRequest.VolleyCallback callback){
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

    public void getFollowing(final FollowersRequest.VolleyCallback callback){
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

    public void countLikes(final FollowersRequest.VolleyCallback callback){
        final int post_id;

        post_id = this.post_id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_COUNT_POST_LIKES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                        //if success change like color to red if not change to white
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
                params.put("post_id", String.valueOf(post_id));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void getPostListLikes(final FollowersRequest.VolleyCallback callback){
        final int post_id;

        post_id = this.post_id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_LIKE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                        //if success change like color to red if not change to white
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
                params.put("post_id", String.valueOf(post_id));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void likePost(final FollowersRequest.VolleyCallback callback){
        final int user_id;
        final int post_id;

        user_id = KEY_ID;
        post_id = this.post_id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LIKE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                        //if success change like color to red if not change to white
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
                params.put("user_id", String.valueOf(user_id));
                params.put("post_id", String.valueOf(post_id));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void unLikePost(final FollowersRequest.VolleyCallback callback){
        final int USER_ID;
        final int FOLLOWER_ID;

        USER_ID = KEY_ID;
        FOLLOWER_ID = this.follower_id;

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
