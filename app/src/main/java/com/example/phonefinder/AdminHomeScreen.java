package com.example.phonefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AdminHomeScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Boolean isOpen=false;
    Button view,newItem;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        newItem = (Button) findViewById(R.id.addItem);
        newItem.setOnClickListener(this);

        view = (Button) findViewById(R.id.viewItems);
        view.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference  = FirebaseDatabase.getInstance().getReference("admin");
        userId = user.getUid();

        Button lgOut = (Button) findViewById(R.id.logout);
        lgOut.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.addItem:
                startActivity(new Intent(this, addItem.class));
                break;
            case R.id.viewItems:
                startActivity(new Intent(this, ItemsActivity.class));
                break;

        }
    }


}