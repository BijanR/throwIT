package de.fh_dortmund.throwit.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import de.fh_dortmund.throwit.R;
import de.fh_dortmund.throwit.menu.DeviceInformation;
import de.fh_dortmund.throwit.menu.Menu;

/**
 * @author Bijan Riesenberg
 */
public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "Sign in";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmail = (EditText) findViewById(R.id.edit_mail);
        etPass = (EditText) findViewById(R.id.edit_password);
        final Button bLogin = (Button) findViewById(R.id.btn_login);
        final Button registerLink = (Button) findViewById(R.id.btn_register);
        final Button play = (Button) findViewById(R.id.no_user);
        final Button device = (Button) findViewById(R.id.device);


        mAuth = FirebaseAuth.getInstance(); // Get reference to the Firebase auth object


        // Listener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Log.d(TAG, "Signed in: " + user.getUid());
                }
                else {
                    Log.d(TAG, "Currently signed out");
                }
            }
        };


/*
        registerLink.setOnClickListener(new View.OnClickListener(){ // Creating a listener which leads you from the start page to the register page
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(Login.this, RegisterActivity.class);
                Login.this.startActivity(registerIntent);
            }
        });
*/

        registerLink.setOnClickListener(new View.OnClickListener(){ // Creating a listener which leads you from the start page to the register page
            @Override
            public void onClick(View v){
               createUserAccount();
            }
        });


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUserIn();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUserIn();
            }
        });

        device.setOnClickListener(new View.OnClickListener() { // Getting device information
           @Override
           public void onClick(View v){
               DeviceInformation d = new DeviceInformation();
               Log.i("device: ",d.getDeviceName());
           }
        });

    }

    /**
     * Send Intent to Start Menu Activity.
     */
    private void launchMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }



    /**
     *  Add and remove Listener
     */
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop (){
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateStatus(){
       // launchMenu();
        TextView status = (TextView) findViewById(R.id.tvStatus);
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            status.setText("Signed in" + user.getEmail());
        }
        else {
            status.setText("Signed out");
        }

    }

    /**
     *  Method to create an User Account and give a toast if the creation was successful
     */
    private void createUserAccount(){

      String mail = etEmail.getText().toString();
      String password = etPass.getText().toString();

      try {
          mAuth.createUserWithEmailAndPassword(mail, password)// Using CreateUserWithEmailAndPassword Method --> Google Firebase
                  .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {

                     /* // Counting the number of characters of the password
                      String password = etPass.getText().toString();
                      int count = 0;
                      for(int i = 0; i < password.length(); i++){
                          count += 1;
                      } */

                          if (task.isSuccessful()) {
                              Toast.makeText(Login.this, "User was created", Toast.LENGTH_SHORT).show();
                          }
                       /* else if(count < 6){
                          Toast.makeText(Login.this, "Password needs at least 6 characters", Toast.LENGTH_SHORT).show();

                      } */
                          else {
                              Toast.makeText(Login.this, "CreationFailed", Toast.LENGTH_SHORT).show();
                          }
                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          if (e instanceof FirebaseAuthUserCollisionException) {
                              Toast.makeText(Login.this, "This E-Mail is already in use", Toast.LENGTH_SHORT).show();
                          } else {
                              Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
      }catch (IllegalArgumentException e) {
        Toast.makeText(Login.this, "Username or Password can't be empty", Toast.LENGTH_SHORT).show();
      }

    }


    private void signUserIn(){
        String mail = etEmail.getText().toString();
        String password = etPass.getText().toString();
        try {
            mAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                launchMenu();
                            } else {
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                            updateStatus();
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(Login.this, "No Account with this E-Mail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (IllegalArgumentException iae) {
            Toast.makeText(Login.this, "Username or Password can't be empty", Toast.LENGTH_SHORT).show();
        }
    }


    private void signOut(){ // there is no sign out Button but we have the method already
        mAuth.signOut();
        updateStatus();
    }

}




