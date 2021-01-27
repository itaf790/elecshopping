package com.example.elechshopping.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elechshopping.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtProductName,txtProductDesc, txtProductQuantity, txtProductPrice , txtProductBrand , txtProductDate , txtProductNameEx, txtProductPriceEx;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView= (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName= (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice= (TextView) itemView.findViewById(R.id.product_price);
     //   txtProductDesc= (TextView) itemView.findViewById(R.id.product_description);

        txtProductNameEx = itemView.findViewById(R.id.cart_product_name);
       txtProductPriceEx = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductDate = itemView.findViewById(R.id.cart_product_date);
        txtProductBrand = itemView.findViewById(R.id.cart_product_brand);


    }


    public void setItemClickListner(ItemClickListner listner){

        this.listner=listner;

    }
    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(),false);

    }

}
