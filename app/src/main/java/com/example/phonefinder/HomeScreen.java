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

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Boolean isOpen=false;
    Button newProd,saved,display;
    ImageView imageView;
    //CurrentUser.getInstance().getCurrentUser()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        saved = (Button) findViewById(R.id.savedPurchases);
        saved.setOnClickListener(this);

        newProd = (Button) findViewById(R.id.button5);
        newProd.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference  = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();

        TextView accName =(TextView) findViewById(R.id.textView2);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String accountName = userProfile.getFirstName();
                    accName.setText(accountName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeScreen.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

        Button lgOut = (Button) findViewById(R.id.logout);
        lgOut.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button5:
                startActivity(new Intent(this, UserProductView.class));
                break;
                case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.savedPurchases:

                startActivity(new Intent(this, purchasesProductView.class));
                break;





        }
    }


}