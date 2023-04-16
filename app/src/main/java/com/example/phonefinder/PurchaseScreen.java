package com.example.phonefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PurchaseScreen extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private String userId;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference2;
    private ValueEventListener dbListener;
    private List<Upload> uploads;
    private LinearLayout phonesContainer;
    private TextView totalPrice;
    private int total;
    private Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_screen);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        uploads = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("checkout").child(userId);
        totalPrice = findViewById(R.id.totalPrice);
        phonesContainer = findViewById(R.id.phonesContainer);
        order = (Button)findViewById(R.id.placeOrder);
        order.setOnClickListener(this);
        dbListener = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uploads.clear();
                phonesContainer.removeAllViews();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);
                    displayPhoneItem(upload);
                }
                updateTotalPrice();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PurchaseScreen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPhoneItem(Upload upload) {
        LinearLayout itemContainer = new LinearLayout(this);
        itemContainer.setOrientation(LinearLayout.VERTICAL);

        // Create TextViews for the phone model, quantity, and price
        TextView phoneMan = new TextView(this);
        TextView phoneQuan = new TextView(this);
        TextView phonePrice = new TextView(this);
        TextView phoneName = new TextView(this);
        Button remove = new Button(this);


        View v = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.setMargins(0, 16, 0, 16);
        v.setLayoutParams(layoutParams);
        v.setBackgroundColor(Color.parseColor("#FFD3D3D3"));


        phoneName.setText("Name: " + upload.getName());
        phoneName.setTextColor(Color.BLACK);
        phoneName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        phoneMan.setText("Manufacturer: " + upload.getManufacturer());
        phoneMan.setTextColor(Color.BLACK);
        phoneMan.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        phoneQuan.setText("Quantity: " + upload.getStock());
        phoneQuan.setTextColor(Color.BLACK);
        phoneQuan.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        phonePrice.setText("Price: €" + upload.getPrice());
        phonePrice.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Add the TextViews to the phones container
        itemContainer.addView(phoneName);
        itemContainer.addView(phoneMan);
        itemContainer.addView(phoneQuan);
        itemContainer.addView(phonePrice);
        itemContainer.addView(remove);
        itemContainer.addView(v);


        phonesContainer.addView(itemContainer);
        remove.setText("delete");

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference2.child(upload.getKey()).removeValue();
                phonesContainer.removeView(itemContainer);
                uploads.remove(upload);
                updateTotalPrice();
                Toast.makeText(PurchaseScreen.this, "Item Removed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateTotalPrice() {
        total = 0;
        for (Upload upload : uploads) {
            total += Integer.parseInt(upload.getPrice());
        }
        totalPrice.setText("Total Price: €" + total);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeOrder:
                validateCard();
                break;
        }
    }

    private void validateCard() {
        TextView customerName = (TextView) findViewById(R.id.Name);
        TextView customerNumber = (TextView) findViewById(R.id.card);
        TextView cardMonth = (TextView) findViewById(R.id.month);
        TextView cardYear = (TextView) findViewById(R.id.year);
        TextView cardCvv = (TextView) findViewById(R.id.cvv);

        String cn = customerName.getText().toString().trim();
        String c= customerNumber.getText().toString().trim();
        String m = cardMonth.getText().toString().trim();
        String y = cardYear.getText().toString().trim();
        String cVV= cardCvv.getText().toString().trim();


        if (cn.isEmpty()) {
            customerName.setError("Card holders name required");
            customerName.requestFocus();
            return;
        }
        if (c.isEmpty()|| c.length()!=16) {
            customerNumber.setError("Incorrect amount");
            customerNumber.requestFocus();
            return;

        }
        if (m.isEmpty() || m.length()!=2) {
            cardMonth.setError("month is required. /mm/ ");
            cardMonth.requestFocus();
            return;
        } if (y.isEmpty() || y.length()!=4) {
            cardYear.setError("year is required /yyyy");
            cardYear.requestFocus();
            return;
        } if (cVV.isEmpty() || cVV.length()!=3) {
            cardCvv.setError("cvv is required and only 3 numbers");
            cardCvv.requestFocus();
            return;
        }
        if(total==0){
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(PurchaseScreen.this,ConfirmationScreen.class));


    }
}