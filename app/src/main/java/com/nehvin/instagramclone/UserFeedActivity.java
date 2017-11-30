package com.nehvin.instagramclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ImageView imgView = new ImageView(getApplicationContext());
        imgView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        imgView.setImageDrawable(getResources().getDrawable(R.drawable.instagramlogo));
        linearLayout.addView(imgView);

        Intent intent = getIntent();
        String activeUser = intent.getStringExtra("username");
        setTitle(activeUser + "'s feed");

        ParseQuery<ParseObject> selectedUserImages = new ParseQuery<ParseObject>("Image");
        selectedUserImages.whereEqualTo("username", activeUser);
        selectedUserImages.orderByDescending("updatedAt");
        selectedUserImages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects != null)
                {
                    if(objects.size() > 0 )
                    {
                        for(ParseObject object : objects){
                            ParseFile file = object.getParseFile("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if( e == null && data !=null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        ImageView imgView = new ImageView(getApplicationContext());
                                        imgView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        imgView.setImageBitmap(bitmap);
                                        linearLayout.addView(imgView);
                                    }
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(UserFeedActivity.this, "No images found for the selected user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
