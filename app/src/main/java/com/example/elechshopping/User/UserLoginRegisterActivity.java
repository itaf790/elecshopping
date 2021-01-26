package com.example.elechshopping.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elechshopping.LoginActivity;
import com.example.elechshopping.R;
import com.example.elechshopping.registerActivity;


public class UserLoginRegisterActivity extends AppCompatActivity {



    private TextView login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_register);

        login=(TextView) findViewById(R.id.addlog);
        register=(TextView) findViewById(R.id.addregister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginRegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginRegisterActivity.this, registerActivity.class);
                startActivity(intent);

            }
        });


    }
}