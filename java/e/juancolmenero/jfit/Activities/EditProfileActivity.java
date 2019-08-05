package e.juancolmenero.jfit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;
import e.juancolmenero.jfit.VolleyRequest.MySingleton;
import e.juancolmenero.jfit.VolleyRequest.URLs;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class EditProfileActivity extends AppCompatActivity {
    int KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final Button submit_edit_button;
        final TextView name_tv, status_tv;
        final TextView name_lbl, status_lbl;

        name_lbl = findViewById(R.id.name_lbl);
        status_lbl = findViewById(R.id.status_lbl);

        Intent intent = getIntent();
        name_lbl.setText(intent.getStringExtra("name"));
        status_lbl.setText(intent.getStringExtra("status"));

        //if the user is already logged in we will directly start the profile activity
        KEY_ID = SharedPrefManager.getInstance(this).getUser().getId();

        //TODO: Eventually add image!!!
        submit_edit_button = findViewById(R.id.submit_profile_button);
        name_tv = findViewById(R.id.edit_name_tv);
        status_tv =  findViewById(R.id.edit_status_tv);
        name_tv.setText(intent.getStringExtra("name"));
        status_tv.setText(intent.getStringExtra("status"));

        submit_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, status, image;
                name = name_tv.getText().toString();
                status = status_tv.getText().toString();
                updateProfile(name, status);
            }
        });

    }

    public void updateProfile(String name, String status){
        final String mName = name;
        final String mStatus = status;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resultObject = new JSONObject(response);
                            if(resultObject.getString("message").equals("Profile Updated")){
                                Toast.makeText(getApplicationContext(), resultObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("user_id", String.valueOf(KEY_ID));
                params.put("name", String.valueOf(mName));
                params.put("status", String.valueOf(mStatus));
                return params;
            }
        };

        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
        Intent backToMain = new Intent(this, MainActivity.class);
        backToMain.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(backToMain);
    }
}
