package com.example.phonefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.recaptcha.Recaptcha;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity implements ImageAdapter.OnItemCLickListener {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private ValueEventListener dbListener;
    private List<Upload> uploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar=findViewById(R.id.progress_circle);
        uploads = new ArrayList<>();
        adapter = new ImageAdapter(ItemsActivity.this, uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ItemsActivity.this);
        firebaseStorage=FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("items");

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
                Toast.makeText(ItemsActivity.this,"Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void OnItemClick(int position) {

        Toast.makeText(this, "Items in stock " +uploads.get(position).getStock(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditCLick(int position) {
      //  Toast.makeText(this, "edit click"+position, Toast.LENGTH_SHORT).show();
        Upload selectedItem = uploads.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference itemRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.item_dialog, null);


        EditText pName = dialogView.findViewById(R.id.Name);
        EditText pCategory = dialogView.findViewById(R.id.category);
        EditText pStock = dialogView.findViewById(R.id.stock);
        EditText pManufacturer = dialogView.findViewById(R.id.manufacturer);
        EditText pPrice = dialogView.findViewById(R.id.price);

        AlertDialog.Builder itmDialog = new AlertDialog.Builder(this);
        itmDialog.setTitle("Update Item");
        itmDialog.setView(dialogView);
        itmDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String phoneName = pName.getText().toString();
                String phoneManufacturer = pManufacturer.getText().toString();
                String phoneStock = pStock.getText().toString();
                String phoneCategory =pCategory.getText().toString();
                String phonePrice = pPrice.getText().toString();


                databaseReference.child(selectedKey).child("name").setValue(phoneName);
                databaseReference.child(selectedKey).child("category").setValue(phoneCategory);
                databaseReference.child(selectedKey).child("manufacturer").setValue(phoneManufacturer);
                databaseReference.child(selectedKey).child("stock").setValue(phoneStock);
                databaseReference.child(selectedKey).child("price").setValue(phonePrice);
                adapter.notifyDataSetChanged();
            }
        });
        itmDialog.setNegativeButton("Cancel",null);

        itmDialog.show();

    }

    @Override
    public void onDeleteCLick(int position) {
        //Toast.makeText(this, "delete click"+position, Toast.LENGTH_SHORT).show();
        Upload selectedItem = uploads.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference itemRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        itemRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(ItemsActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(dbListener);
    }
}