package com.example.phonefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmationScreen extends AppCompatActivity implements View.OnClickListener {
    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        home = (Button)findViewById(R.id.HomeScreen);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.HomeScreen:
                startActivity(new Intent(this, HomeScreen.class));
                break;
        }
    }
}