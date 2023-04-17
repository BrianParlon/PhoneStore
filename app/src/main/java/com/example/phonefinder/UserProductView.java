package com.example.phonefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserProductView extends AppCompatActivity implements UserItemAdapter.OnItemCLickListener {

    private RecyclerView recyclerView;
    private UserItemAdapter adapter;
    private FloatingActionButton cart;
    private FirebaseUser user;
    private String userId;
    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference,databaseReference2;
    private ValueEventListener dbListener;
    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cart = findViewById(R.id.cartButton);
        progressBar=findViewById(R.id.progress_circle);
        uploads = new ArrayList<>();
        adapter = new UserItemAdapter(UserProductView.this, uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(UserProductView.this);
        firebaseStorage=FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("items");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("checkout").child(userId);

        dbListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                uploads.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UserProductView.this,"Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProductView.this, CartItemsView.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void OnItemClick(int position) {
            Toast.makeText(this, "Amount in stock: " +uploads.get(position).getStock(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseCLick(int position) {
        int stock = Integer.parseInt(uploads.get(position).getStock());
        if(stock==0){
            Toast.makeText(this, "This item is currently out of stock", Toast.LENGTH_SHORT).show();
            return;
        }
        Upload selectedItem = uploads.get(position);
        int quantity=1;
        uploads.get(position).setQuantity(String.valueOf(quantity));
        Upload upload = new Upload(uploads.get(position).getName().trim(),uploads.get(position).getImageUrl().trim(),uploads.get(position).getCategory().trim(),uploads.get(position).getManufacturer().trim(),uploads.get(position).getStock().trim(),uploads.get(position).getPrice().trim(),uploads.get(position).getQuantity());

        String uploadId = databaseReference2.push().getKey();
        databaseReference2.child(uploadId).setValue(upload);


        Toast.makeText(this, "Phone has been added to cart "+selectedItem.getName(), Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(dbListener);
    }
}