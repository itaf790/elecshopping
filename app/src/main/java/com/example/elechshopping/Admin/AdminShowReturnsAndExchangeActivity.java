package com.example.elechshopping.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elechshopping.Model.AdminOrders;
import com.example.elechshopping.Model.Policies;
import com.example.elechshopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminShowReturnsAndExchangeActivity extends AppCompatActivity {


    private TextView txtreason, txtchoose;
    private ImageView closeTextBtn;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
    private String polID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_returns_and_exchange);

        closeTextBtn = (ImageView) findViewById(R.id.close);
        closeTextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        polID = getIntent().getStringExtra("polid");

        txtreason = (TextView) findViewById(R.id.txtreason);
        txtchoose = (TextView) findViewById(R.id.txtchoose);

        polID=getIntent().getStringExtra("pid");


        getProductDetails(polID);

        final DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Exchange and Returns");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    AdminOrders admin = dataSnapshot.getValue(AdminOrders.class);
                    txtchoose.setText(admin.getSelect());
                    txtreason.setText(admin.getReason());

                }

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                throw databaseError.toException();

            }
        });


    }

    private void getProductDetails(String polID) {
    }
}