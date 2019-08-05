package e.juancolmenero.jfit.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;

public class IntroductionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void OnLogin(View view){
        Intent intent = new Intent (this, SignInActivity.class);
        startActivity(intent);
    }

    public void OnRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
