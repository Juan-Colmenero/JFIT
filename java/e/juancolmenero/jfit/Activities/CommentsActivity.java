package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.Map;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.CommentsAdapter;
import e.juancolmenero.jfit.Models.CommentsModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.CommentsRequest;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

public class CommentsActivity extends AppCompatActivity {

    private int KEY_ID, post_id;
    private String REQUEST_ID;

    //RecyclerView verticalRecyclerView;
    private RecyclerView mCommentsRecyclerView;

    //VerticalRecyclerViewAdapter adapter;
    private CommentsAdapter mAdapter;
    private Set<String> setOfKeySet;
    private ArrayList<CommentsModel> arrayListComments;

    private Button sendComment_btn;
    private EditText comment_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle extras = getIntent().getExtras();
        String requestID = "";
        if (extras != null) {
            requestID = getIntent().getStringExtra("request");
            //TODO: CHECK IF REQUEST_ID IS NEEDED, IF NOT DELETE WHERE APPEARS
            REQUEST_ID = requestID;
            post_id = getIntent().getIntExtra("post_id", 0);
        }

        sendComment_btn = (Button) findViewById(R.id.send_comment_btn);
        comment_input = (EditText) findViewById(R.id.comment_et);

        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();

        //CREATE AND DISPLAY RECYCLER VIEW
        arrayListComments = new ArrayList<>();
        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.comments_recyclerView);
        mCommentsRecyclerView.setHasFixedSize(true);
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new CommentsAdapter(this, arrayListComments);
        mCommentsRecyclerView.setAdapter(mAdapter);

        displayComments(requestID);

        sendComment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(comment_input.getText().toString())) {
                    comment_input.setError("Please enter comment");
                    comment_input.requestFocus();
                    return;
                }
                final String user_input = comment_input.getText().toString();
                System.out.println(user_input);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEND_COMMENT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                System.out.println("COMMENT INSERTED");
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
                        params.put("comment", user_input);
                        params.put("user_id", String.valueOf(KEY_ID));
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                removeAllItems();
                displayComments("commentsList");
                comment_input.getText().clear();
            }
        });

    }

    private void removeAllItems() {
        arrayListComments.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void displayComments(String request_id){

        if (request_id.equals("commentsList")){
            CommentsRequest commentsRequest = new CommentsRequest(CommentsActivity.this, KEY_ID);
            commentsRequest.setPost_id(post_id);
            commentsRequest.getComments(new CommentsRequest.VolleyCallback(){
                @Override
                public void onSuccess(String result){
                    System.out.println(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray commentsArray = jsonObject.getJSONArray("comments");

                        for(int i = 0; i < commentsArray.length(); i++){
                            JSONObject commentsObject = commentsArray.getJSONObject(i);
                            CommentsModel commentsModel = new CommentsModel();
                            commentsModel.setPost_id(post_id);
                            commentsModel.setComment_id(commentsObject.getInt("comment_id"));
                            commentsModel.setUser_id(commentsObject.getInt("user_id"));
                            commentsModel.setCommentMessage(commentsObject.getString("comment"));
                            commentsModel.setName(commentsObject.getString("username"));
                            arrayListComments.add(commentsModel);
                            /*      "comment_id":1,
                                    "comment":"How long was this workout???",
                                    "user_id":10,
                                    "username":"abc10",
                                    "name":"ABC NAME"*/

                        }

                        mAdapter.notifyDataSetChanged();
                        System.out.println(arrayListComments);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }
            });
        }
    }
}
