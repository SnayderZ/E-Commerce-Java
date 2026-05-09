package com.proyecto.e_commerce_java.presentation.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public interface OnProductActionListener {
        void onAddToCart(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final OnProductActionListener listener;

    public ProductAdapter(OnProductActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Product> newProducts) {
        products.clear();

        if (newProducts != null) {
            products.addAll(newProducts);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImage;
        private final TextView productNameText;
        private final TextView productStockText;
        private final TextView productPriceText;
        private final Button addToCartButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productNameText = itemView.findViewById(R.id.productNameText);
            productStockText = itemView.findViewById(R.id.productStockText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }

        void bind(Product product, OnProductActionListener listener) {
            Context context = itemView.getContext();

            productNameText.setText(product.getName());
            productStockText.setText(context.getString(R.string.stock_format, product.getStock()));
            productPriceText.setText(context.getString(R.string.price_format, product.getPrice()));

            loadProductImage(context, product.getImage(), productImage);

            addToCartButton.setOnClickListener(view -> listener.onAddToCart(product));
        }

        private void loadProductImage(Context context, String imageText, ImageView imageView) {
            if (imageText == null || imageText.trim().isEmpty()) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
                return;
            }

            if (imageText.startsWith("http")) {
                Glide.with(context)
                        .load(imageText)
                        .centerCrop()
                        .placeholder(R.drawable.bg_product_placeholder)
                        .into(imageView);
                return;
            }

            try {
                byte[] imageBytes = Base64.decode(imageText, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception exception) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
