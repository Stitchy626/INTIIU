package rokuniroku.code.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

public class LoginPageNew extends BaseActivity {

    private static final String TAG = "SENPAI";
    private static final int RC_SIGN_IN = 9001;
    private static String userValidation = "@student.newinti.edu.my";

    private FirebaseAuth mAuth;
    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabase;

    private SignInButton buttonSignInStudent, buttonSignInClub;

    private GoogleSignInClient mGoogleSignInClient;

    private Boolean bIsClub, bIsClubClick;

    private ArrayList<String> arrayEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_new);

        mAuth = FirebaseAuth.getInstance();

        rootDatabase = dbDatabase.getInstance().getReference().child("Club");

        buttonSignInClub = (SignInButton) findViewById(R.id.buttonSignInClub);
        buttonSignInStudent = (SignInButton)findViewById(R.id.buttonSignInStudent);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        bIsClub = false;
        bIsClubClick = false;

        arrayEmail = new ArrayList<>();

        //start
        rootDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    arrayEmail.add(snapshot.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonSignInStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bIsClubClick = false;
                SignIn();
            }
        });

        buttonSignInClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
                bIsClubClick = true;
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
                // Google Sign In failed
                Toast.makeText(LoginPageNew.this, "Google sign in failed, Please try again later", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            bIsClub = false;

                            String email = user.getEmail().toString();
                            int startZ = 0;

                            for (int x = 0; x < email.length(); x++) {
                                if (email.charAt(x) == '@') {
                                    startZ = x;
                                    break;
                                }
                            }

                            String backEmail = email.substring(startZ, email.length());
                            final String frontEmail = email.substring(0, startZ).trim();

                            for(int x = 0; x < arrayEmail.size(); x++){
                                if(frontEmail.equals(arrayEmail.get(x))){
                                    bIsClub = true;
                                    break;
                                }
                            }

                            if (backEmail.equals(userValidation) && bIsClubClick == false || bIsClub == true && bIsClubClick == true) {
                                startActivity(new Intent(LoginPageNew.this, HomePage.class));
                                Toast.makeText(LoginPageNew.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "signInWithCredential:success");
                                finish();
                            }else{
                                SignOut();
                            }
                        }else {
                            Toast.makeText(LoginPageNew.this, "Failed to sign in with credential", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failed", task.getException());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void SignIn() {
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
                        if(bIsClubClick == true) {
                            Toast.makeText(LoginPageNew.this, "Invalid Club credential", Toast.LENGTH_LONG).show();
                            bIsClubClick = false;
                        }
                        else
                            Toast.makeText(LoginPageNew.this, "Please sign in with an INTI email", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
