package e.juancolmenero.jfit.VolleyRequest;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentsRequest {

    private int KEY_ID;
    private Activity context;
    private int post_id;

    public CommentsRequest(Activity context, int KEY_ID){
        this.context = context;
        this.KEY_ID = KEY_ID;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }


    public void getComments(final CommentsRequest.VolleyCallback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_COMMENTS,
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
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_SHORT).show();
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

    public void sendComment(final CommentsRequest.VolleyCallback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_COMMENTS,
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
                params.put("post_id", String.valueOf(post_id));
                return params;
            }
        };

        MySingleton.getInstance(context.getApplication().getApplicationContext()).addToRequestQueue(stringRequest);

    }


    public interface VolleyCallback{
        void onSuccess(String result);
    }

}
