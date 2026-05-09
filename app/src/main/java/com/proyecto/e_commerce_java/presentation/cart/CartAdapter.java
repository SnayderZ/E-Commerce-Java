package com.proyecto.e_commerce_java.presentation.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface OnCartActionListener {
        void onIncrease(CartItem item);

        void onDecrease(CartItem item);

        void onRemove(CartItem item);
    }

    private final List<CartItem> cartItems = new ArrayList<>();
    private final OnCartActionListener listener;

    public CartAdapter(OnCartActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CartItem> newItems) {
        cartItems.clear();

        if (newItems != null) {
            cartItems.addAll(newItems);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView cartProductNameText;
        private final TextView cartProductPriceText;
        private final TextView quantityText;
        private final Button increaseQuantityButton;
        private final Button decreaseQuantityButton;
        private final Button removeButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductNameText = itemView.findViewById(R.id.cartProductNameText);
            cartProductPriceText = itemView.findViewById(R.id.cartProductPriceText);
            quantityText = itemView.findViewById(R.id.quantityText);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        void bind(CartItem item, OnCartActionListener listener) {
            cartProductNameText.setText(item.getProduct().getName());
            cartProductPriceText.setText(
                    itemView.getContext().getString(R.string.price_format, item.getProduct().getPrice())
            );
            quantityText.setText(String.valueOf(item.getQuantity()));

            increaseQuantityButton.setOnClickListener(view -> listener.onIncrease(item));
            decreaseQuantityButton.setOnClickListener(view -> listener.onDecrease(item));
            removeButton.setOnClickListener(view -> listener.onRemove(item));
        }
    }
}
