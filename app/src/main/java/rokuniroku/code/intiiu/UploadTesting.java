package rokuniroku.code.intiiu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadTesting extends AppCompatActivity {

    //Storage
    private FirebaseStorage dbStorage;
    private StorageReference rootStorage;

    //Realtime Database
    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabase;


    private Button buttonUpload;
    private ImageView imageViewBanner;

    private static final int GALLERY_INTENT = 1;
    private ProgressDialog progressDialog;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_testing);

        rootStorage = dbStorage.getInstance().getReference();
        rootDatabase = dbDatabase.getInstance().getReference();

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);

        progressDialog = new ProgressDialog(this);


    //enter select image from storage
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DownloadImage();
                Intent intent = new Intent();
                intent.setAction(intent.ACTION_PICK);
                intent.setType("image/*");//allow only images to be selected
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewBanner.setImageBitmap(bitmap);
            }catch(IOException e){
                Log.d("Error =", e.getMessage().toString());
            }
            UploadImage();
        }
    }

    //push data to storage
    private void UploadImage(){
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();

        //String id = rootDatabase.push().getKey();

        //StorageReference newStorage = rootStorage.child(id);

        StorageReference path = rootStorage.child("Announcement").child("EventAnn").child("-LH1fSpD3YprEZ4jatha");

        UploadTask uploadImage = path.putFile(uri);

        uploadImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadTesting.this, "Uploaded", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //if fail
            }
        });
    }


    //pull image from storage and link to imageView xml
    private void DownloadImage(){
        StorageReference path = rootStorage.child("Announcement").child("INTIAnn").child("101010");

        //progressDialog.setMessage("Downloading ....");
        //progressDialog.show();

        Glide.with(UploadTesting.this)
                .using(new FirebaseImageLoader())
                .load(path)
                .into(imageViewBanner);
    }
}
