package com.example.elechshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.elechshopping.Admin.AdminDelivaryTimeActivity;
import com.example.elechshopping.Admin.AdminHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ChooseItemActivity extends AppCompatActivity {


    private ImageView closeTextBtn;
    private Button next , btnDisplay;


    private TextView  txtMsg1 ;
    private EditText reson, select ;
    EditText writedown;
    private String productID = "", state = "Normal";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private ProgressDialog loadingBar;
    int count = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);

        closeTextBtn = (ImageView) findViewById(R.id.close);
        closeTextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });



        next=(Button)findViewById(R.id.next);
        reson=(EditText) findViewById(R.id.writereason);
        //select=(EditText) findViewById(R.id.edchoose);



        loadingBar= new ProgressDialog(this);

        ////// had firebase ll login and signin
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addingreason();
            }
        });

    }



    private void Addingreason() {
        String reason= reson.getText().toString() ;
        String choose= select.getText().toString() ;

        if (TextUtils.isEmpty(reason)){
            Toast.makeText(this, "Please Choose one", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(choose)){
            Toast.makeText(this, "Please write your reason", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Adding Reason");
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            addreason(reason);
        }

    }

    private void addreason(final String reason) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Admin").child(reason).exists())) {
                    HashMap<String, Object> userdataMap= new HashMap<>();
                    userdataMap.put("reason",reason);
                   // userdataMap.put("select",select);


                    RootRef.child("Admin").child("Exchange or Returns").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(ChooseItemActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(ChooseItemActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                            else {
                                loadingBar.dismiss();
                                Toast.makeText(ChooseItemActivity.this, "Network Error: Please try again  ", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                } else {
                    Toast.makeText(ChooseItemActivity.this, "This already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(ChooseItemActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChooseItemActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}