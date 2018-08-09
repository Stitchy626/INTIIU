package rokuniroku.code.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends BaseActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static String emailValidation = "@student.newinti.edu.my", clubValidaton = "@club.newinti.edu.my";

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private Button buttonSignInClub,buttonResetPassword;
    private SignInButton buttonSignInGoogle;
    private EditText editTextUsername,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        buttonResetPassword=(Button) findViewById(R.id.buttonResetPassword);
        buttonSignInClub = (Button) findViewById(R.id.buttonSignInClub);
        buttonSignInGoogle = (SignInButton)findViewById(R.id.buttonSignInGoogle);
        editTextUsername=(EditText) findViewById(R.id.editTextUsername);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,ResetPasswordActivity.class));
            }
        });

        buttonSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignIn();
            }
        });

        buttonSignInClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginClub();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            String email = user.getEmail().toString();
                            int startZ = 0;

                            for (int x = 0; x < email.length(); x++) {
                                if (email.charAt(x) == '@') {
                                    startZ = x;
                                    break;
                                }
                            }

                            email = email.substring(startZ, email.length());

                            if (email.equals(emailValidation)) {
                                startActivity(new Intent(LoginPage.this, HomePage.class));
                                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_LONG).show();

                                //startActivity(new Intent(LoginPage.this, LogoutPage.class));
                            }else{
                                SignOut();
                            }
                        }else {
                                SignOut();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void UserSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void SignOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoginPage.this, "Please login with INTI email", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void LoginClub(){
        if(editTextUsername.getText().toString().equals("")|| editTextPassword.getText().toString().equals(""))
        {
            Toast.makeText(LoginPage.this,"Blank fields are not allowed",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(editTextUsername.getText().toString(),editTextPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginPage.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginPage.this,HomePage.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginPage.this,"Login Unsuccessful,Please try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
