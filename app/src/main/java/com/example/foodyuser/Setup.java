package com.example.foodyuser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup extends AppCompatActivity {

    private CircleImageView profilePicture;
    private FloatingActionButton editImage;
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "ProfileImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        init();

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
            }
        });
    }

    private void init(){
        this.profilePicture = findViewById(R.id.profilePicture);
        this.editImage = findViewById(R.id.edit_profile_picture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK){

            //From Gallery
            Uri imageUri = data.getData();
            if(imageUri!=null){
                startCrop(imageUri);
            }

        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){

            Uri imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop != null){
                profilePicture.setImageURI(imageUriResultCrop);
            }
        }
    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),destinationFileName)));

        uCrop.withAspectRatio(1,1);

        uCrop.withMaxResultSize(450,450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(Setup.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options= new UCrop.Options();

        options.setCompressionQuality(70);

        //Compress Type
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);

        //Colors
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));


        options.setToolbarTitle(getResources().getString(R.string.crop_image));

        return options;


    }
}