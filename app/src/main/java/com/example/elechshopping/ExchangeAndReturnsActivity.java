package com.example.elechshopping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.elechshopping.Admin.AdminMaintainProductsActivity;
import com.example.elechshopping.Model.Cart;
import com.example.elechshopping.Model.Products;
import com.example.elechshopping.ViewHolder.CartViewHolder;
import com.example.elechshopping.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import io.paperdb.Paper;

public class ExchangeAndReturnsActivity extends AppCompatActivity {


    private ImageView closeTextBtn;

    private DatabaseReference ProductsRef;
    private String productID = "";
    private RecyclerView recyclerView;
    private TextView txtMsg1;
    private String type="";
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    private ImageView productImage;



    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_and_returns);


        closeTextBtn = (ImageView) findViewById(R.id.close);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(ExchangeAndReturnsActivity.this);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(currentUser.getUid()).child("Products");
        Paper.init(this);
        recyclerView = findViewById(R.id.recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class).build();
        FirebaseRecyclerAdapter adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.ex_re_layout, parent, false);
                        return new ProductViewHolder(view);

                    }
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {


                        holder.txtProductQuantity.setText(" Product Quantity = " + model.getNumberquantity());
                        holder.txtProductPriceEx.setText("Product Price = $" + model.getPrice());
                        holder.txtProductNameEx.setText(" Product Name: " + model.getPname());
                        holder.txtProductBrand.setText("Brand :  " + model.getBrand());
                        holder.txtProductDate.setText("Date:  "+ model.getDate());
                       // Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    Intent intent = new Intent(ExchangeAndReturnsActivity.this, ChooseItemActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                            }
                        });


                    }


                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




}