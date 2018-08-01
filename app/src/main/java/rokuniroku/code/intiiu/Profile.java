package rokuniroku.code.intiiu;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    TextView textViewTest;
    private FirebaseAuth firebaseAuth;
    com.google.android.gms.common.SignInButton signInButton;
    //Sing out button.
    Button SignOutButton;
    // TextView to Show Login User Email and Name.
    TextView LoginUserName, LoginUserEmail;
    public GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        SignOutButton = (Button) findViewById(R.id.sign_out);

        LoginUserName = (TextView) findViewById(R.id.textViewName);

        LoginUserEmail = (TextView) findViewById(R.id.textViewEmail);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

// Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(Profile.this)
                .enableAutoManage(Profile.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        // Showing Log out button.
        SignOutButton.setVisibility(View.VISIBLE);

        // Hiding Login in button.
        signInButton.setVisibility(View.GONE);

        // Showing the TextView.
        LoginUserEmail.setVisibility(View.VISIBLE);
        LoginUserName.setVisibility(View.VISIBLE);

        // Setting up name into TextView.
        LoginUserName.setText("NAME = "+ firebaseUser.getDisplayName().toString());

        // Setting up Email into TextView.
        LoginUserEmail.setText("Email = "+ firebaseUser.getEmail().toString());

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignOutFucntion();

            }
        });
    }

    public void UserSignOutFucntion() {

// Sign Out the User.
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

// Write down your any code here which you want to execute After Sign Out.

// Printing Logout toast message on screen.
                        startActivity(new Intent(Profile.this, auth.class));
                        Toast.makeText(Profile.this, "Logout Successfully", Toast.LENGTH_LONG).show();

                    }
                });


// After logout Hiding sign out button.
        SignOutButton.setVisibility(View.GONE);

// After logout setting up email and name to null.
        LoginUserName.setText(null);
        LoginUserEmail.setText(null);

// After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);
    }

}
