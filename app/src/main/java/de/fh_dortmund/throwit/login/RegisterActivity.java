package de.fh_dortmund.throwit.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import de.fh_dortmund.throwit.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final Button bRegister = (Button) findViewById(R.id.bRegister); // create variable for registration
        final Button back = (Button) findViewById(R.id.back_button);


        back.setOnClickListener(new View.OnClickListener(){ // Creating a listener which leads you from the start page to the register page
            @Override
            public void onClick(View v){
                Intent backbutton = new Intent(RegisterActivity.this, Login.class);
                RegisterActivity.this.startActivity(backbutton);
            }
        });



        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();

                Response.Listener<String> rListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); // Variable is called success

                            if(success){
                                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, Login.class));
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Registration failded")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, password, rListener);

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);


            }


        });
    }
}
