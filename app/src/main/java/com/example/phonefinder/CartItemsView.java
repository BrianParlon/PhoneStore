package com.example.phonefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class CartItemsView extends AppCompatActivity implements CartAdapter.OnItemCLickListener {

    private RecyclerView recyclerView;
    private UserItemAdapter adapter;

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
        setContentView(R.layout.activity_cart_items_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar=findViewById(R.id.progress_circle);
        uploads = new ArrayList<>();
        adapter = new UserItemAdapter(CartItemsView.this, uploads);
        recyclerView.setAdapter(adapter);

        firebaseStorage=FirebaseStorage.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("checkout").child(userId);

        dbListener = databaseReference2.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(CartItemsView.this,"Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(this, "Amount in stock: " +uploads.get(position).getStock(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseCLick(int position) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference2.removeEventListener(dbListener);
    }
}