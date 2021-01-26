package com.example.elechshopping.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elechshopping.HomeActivity;
import com.example.elechshopping.MainActivity;
import com.example.elechshopping.R;


public class AdminHomeActivity extends AppCompatActivity {


    private Button exchangepolicy , returnspolicy, deliverytime , deliveryfee
            ,paymentmethod , logout , addcategory , addproducts , maintan_item
            , show_orders , admindiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        exchangepolicy= (Button)findViewById(R.id.exchangepolicy);
        returnspolicy= (Button)findViewById(R.id.returnspolicy);
        deliveryfee= (Button)findViewById(R.id.admindeliveryfee);
        deliverytime= (Button)findViewById(R.id.admindeliverytime);
        paymentmethod= (Button)findViewById(R.id.adminpaymentmethod);
        logout= (Button)findViewById(R.id.adminlogout);
        addcategory= (Button)findViewById(R.id.add_new_category);
        addproducts= (Button)findViewById(R.id.add_new_item);
        maintan_item= (Button)findViewById(R.id.maintan_item);
        show_orders =(Button)findViewById(R.id.show_orders);
        admindiscount =(Button)findViewById(R.id.discountallproducts);




        addproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminAddProductsActivity.class);
                startActivity(intent);

            }
        });
        show_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckOrdersActivity.class);
                startActivity(intent);

            }
        });

        maintan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        admindiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminDiscountActivity.class);
                startActivity(intent);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        paymentmethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminPaymentMethodActivity.class);
                startActivity(intent);

            }
        });

        deliverytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminDelivaryTimeActivity.class);
                startActivity(intent);

            }
        });

        deliveryfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminDelivaryFeeActivity.class);
                startActivity(intent);

            }
        });

        exchangepolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminExchangeActivity.class);
                startActivity(intent);

            }
        });


        returnspolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminReturnsActivity.class);
                startActivity(intent);

            }
        });


    }

}