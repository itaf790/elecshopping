package com.example.elechshopping;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.elechshopping.Model.Cart;
import com.example.elechshopping.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtMsg1 ,txtTotalAmount, cart_discount  ;
    private double overTotalAmount = 0 ,overTotalAmount1 = 0,  overtotal=0 ,total_after_discount= 0, Total=0, discount= 0 , totalprice=0 ;
    private ImageView closeTextBtn;
    private ProgressDialog loadingBar;
    private String OverTotalAmount = "";
    private String productID = "";





    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        closeTextBtn = (ImageView) findViewById(R.id.close);
        closeTextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        recyclerView = findViewById(R.id.cart_list);
        cart_discount= findViewById(R.id.cart_discount);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = (Button) findViewById(R.id.cart_next);
        txtTotalAmount = (TextView) findViewById(R.id.mycart);
        txtMsg1 = (TextView) findViewById(R.id.msg1);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  //  txtTotalAmount.setText("Total Amount = " + String.valueOf(overTotalAmount));


//                txtTotalAmount.setText("Total Price = R" + String.valueOf(overTotalPrice));
                Intent intent = new Intent(CartActivity.this,PaymentActivity.class);
                intent.putExtra("Total Amount",String.valueOf(overTotalAmount));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        overTotalAmount=0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(currentUser.getUid())
                                .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position, @NonNull final Cart model) {
                        try {

                            cartViewHolder.txtProductQuantity.setText(" Product Quantity = " + model.getNumberquantity());
                            cartViewHolder.txtProductPrice.setText("Product Price = $" + model.getPrice());
                            cartViewHolder.txtProductName.setText(" Product Name: " + model.getPname());
                            cartViewHolder.txtProductBrand.setText("Brand :  " + model.getBrand());
                            cartViewHolder.txtProductTime.setText("Time: "+ model.getTime());
                            cartViewHolder.txtProductDate.setText("Date:  "+ model.getDate());
                            cartViewHolder.txtProductshipped.setText("Shipped Price =  $ "+ model.getDelivery_fee());
                            cartViewHolder.txtProductDiscount.setText("Discount = % "+ model.getDiscount());

                            double oneTypeTotalPrice = (Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getNumberquantity());
                            double oneTypeTotalShipped = (Integer.valueOf(model.getDelivery_fee())) ;


                            if (model.getDiscount().equals("") ){
                                cartViewHolder.txtProductDiscount.setText("Discount = % 0");
                                overtotal = oneTypeTotalPrice + oneTypeTotalShipped;
                                cartViewHolder.txtProducttotalprice.setText("Total Price =  $" + oneTypeTotalPrice);
                                cartViewHolder.txttotalamount.setText("Total Amount = $ " + overtotal);
                                 overTotalAmount1 =overTotalAmount1+ overtotal;
                                //Toast.makeText(CartActivity.this, ""+overTotalAmount1, Toast.LENGTH_SHORT).show();

                            }

                            else {


                                double discount = (Double.valueOf(model.getDiscount())) / 100;
                                overtotal = oneTypeTotalPrice + oneTypeTotalShipped;
                                total_after_discount = overtotal * discount;
                                totalprice = overtotal - total_after_discount;

                                cartViewHolder.txtProducttotalprice.setText("Total Price =  $" + oneTypeTotalPrice);
                                cartViewHolder.txttotalamount.setText("Total Amount = $ " + totalprice);
                            }
                            overTotalAmount = overTotalAmount1+  totalprice;
                            Toast.makeText(CartActivity.this, ""+overTotalAmount, Toast.LENGTH_SHORT).show();
                            txtTotalAmount.setText("Total Price = $" + overTotalAmount);
                            productID=getIntent().getStringExtra("pid");


////////////////////////////////////// this to put totalamount in firebase under cartlist
                            final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

                            final HashMap<String,Object> cartMap = new HashMap<>();
                            cartMap.put("totalAmount", txtTotalAmount.getText().toString());
                            cartListRef.child("User View").child(currentUser.getUid())
                                    .child("totalAmount").updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                cartListRef.child("Admin View").child(currentUser.getUid()).child("totalAmount")
                                                        .updateChildren(cartMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                   Toast.makeText(CartActivity.this, "Total Price = $ "+ overTotalAmount, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }


                                        }
                                    });

                        } catch(NumberFormatException e){
                            return;
                        }




                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {

                                                "Edit Quantity",
                                                "Delete from Cart"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i==0){
                                            Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }

                                        if (i==1){
                                            cartListRef.child("User View")
                                                    .child(currentUser.getUid())
                                                    .child("Products")
                                                    .child(model.getPid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this, "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });





                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderState(){
        DatabaseReference ordersRef;
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(currentUser.getUid());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = (String) dataSnapshot.child("state").getValue();
                    String userName = (String) dataSnapshot.child("name").getValue();

                    if (shippingState != null){
                        if (shippingState.equals("shipped")){

                            txtTotalAmount.setText("Dear " + userName + "\n order is shipped successfully.");
                          //  recyclerView.setVisibility(View.GONE);
                          //  txtMsg1.setVisibility(View.VISIBLE);
                           // txtMsg1.setText("Congratulations, your final order has been Shipped successfully.Soon you will receive your order by your door step ");
                           // nextProcessBtn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more products once you receive your first order", Toast.LENGTH_SHORT).show();

                        } else if (shippingState.equals("not shipped")){
                            txtTotalAmount.setText("Shipping State = not shipped");
                          //  recyclerView.setVisibility(View.GONE);
                          //  txtMsg1.setVisibility(View.VISIBLE);
                          //  nextProcessBtn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more products once you receive your first order", Toast.LENGTH_SHORT).show();


                        }}

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
