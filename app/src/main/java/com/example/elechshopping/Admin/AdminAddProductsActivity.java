package com.example.elechshopping.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.elechshopping.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    String[] payment={"Credit Card","Delivery"};
    String[] namebrand={"Apple","HP" , "Samsung" , "LG", "Dell" , "Sony" };
    String[] namecategory={"Computer","Laptop" , "Tablets" , "Mobiles", "Headphones" , "Cameras" , "TVs" , "Other Electronics"};

    private String categoryName, description, price, pname,saveCurrentDate, saveCurrentTime, brand , paymentmethod, deliverytime, deliveryfee, pquantity, discount;
    private Button addNewProductButton;
    private ImageView inputProductImage;
    private EditText inputProductName ,inputProductDescription, inputProductPrice , inputProductbrand, inputProductpaymentmethod , inputProductdelfee, inputProductDiscount, inputProductdeltime,inputProductquantity ;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String productRandomKey,downLoadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;
    private ImageView closeTextBtn;
    Spinner spin , spinbrand , spincategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_products);
        closeTextBtn = (ImageView) findViewById(R.id.close);
        closeTextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


/////////////// this for payment method spinner
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
         spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,payment);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

//////////////////////////////// this for brand spinner

        spinbrand = (Spinner) findViewById(R.id.spinnerbrand);
        spinbrand.setOnItemSelectedListener(this);
        ArrayAdapter brand = new ArrayAdapter(this,android.R.layout.simple_spinner_item,namebrand);
        brand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinbrand.setAdapter(brand);

        ///////////////////this fot category
        spincategory = (Spinner) findViewById(R.id.spinnercategory);
        spincategory.setOnItemSelectedListener(this);
        ArrayAdapter category  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,namecategory);
        category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spincategory.setAdapter(category);








     //  categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProductButton = (Button) findViewById(R.id.adminadd_new_product);
        inputProductImage = (ImageView) findViewById(R.id.adminproduct_image);
        inputProductName = (EditText) findViewById(R.id.adminproduct_name);
        inputProductDiscount = (EditText) findViewById(R.id.adminproduct_discount);
        inputProductDescription = (EditText) findViewById(R.id.adminproduct_description);
        inputProductPrice = (EditText) findViewById(R.id.adminproduct_price);
       // inputProductbrand = (EditText) findViewById(R.id.adminproduct_brand);
       // inputProductpaymentmethod = (EditText) findViewById(R.id.adminpaymentmethod);
        inputProductdelfee = (EditText) findViewById(R.id.admindeliveryfee);
        inputProductdeltime = (EditText) findViewById(R.id.admindeliverytime);
        inputProductquantity = (EditText) findViewById(R.id.adminproduct_qnt);
        loadingBar = new ProgressDialog(this);


        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateProductData();

            }
        });


    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }
    private void validateProductData() {

        description = inputProductDescription.getText().toString();
        price = inputProductPrice.getText().toString();
        pname = inputProductName.getText().toString();
        deliverytime= inputProductdeltime.getText().toString();
        deliveryfee= inputProductdelfee.getText().toString();
        //paymentmethod= inputProductpaymentmethod.getText().toString();
        pquantity= inputProductquantity.getText().toString();
       // brand= inputProductbrand.getText().toString();
        discount= inputProductDiscount.getText().toString();
        paymentmethod = spin.getSelectedItem().toString();
        brand = spinbrand.getSelectedItem().toString();
        categoryName = spincategory.getSelectedItem().toString();


        if (imageUri == null) {

            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {

            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(price)) {

            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(pname)) {

            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(deliverytime)) {

            Toast.makeText(this, "Please write product delivery time", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(deliveryfee)) {

            Toast.makeText(this, "Please write product delivery fee", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(brand)) {

            Toast.makeText(this, "Please write product brand", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(paymentmethod)) {

            Toast.makeText(this, "Please write payment method", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(categoryName)) {

            Toast.makeText(this, "Please write category name", Toast.LENGTH_SHORT).show();
        }



        else{
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin , Please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calender = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddProductsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductsActivity.this, "Product Image uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }

                        downLoadImageUrl = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){
                            downLoadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddProductsActivity.this, "got the Product image Url Successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }

                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object>  productMap  = new HashMap<>();
        productMap.put("pid" , productRandomKey);
        productMap.put("date" , saveCurrentDate);
        productMap.put("time" , saveCurrentTime);
        productMap.put("description" , description);
        productMap.put("image" , downLoadImageUrl);
        productMap.put("category" , categoryName);
        productMap.put("price" , price);
        productMap.put("pname" , pname);
        productMap.put("pquantity" , pquantity);
        productMap.put("payment_method" , paymentmethod);
        productMap.put("delivery_fee" , deliveryfee);
        productMap.put("delivery_time" , deliverytime);
        productMap.put("brand" , brand);
        productMap.put("discount" , discount);

        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(AdminAddProductsActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddProductsActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddProductsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}