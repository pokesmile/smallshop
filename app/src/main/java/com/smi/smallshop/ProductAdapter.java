package com.smi.smallshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products = new ArrayList<>();
    private Callback sumUpdateCallback;

    ProductAdapter(final Callback supplier) {
        sumUpdateCallback = supplier;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;

        ProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.itemTextViewProductName);
            productPrice = itemView.findViewById(R.id.itemTextViewProductPrice);
        }
    }

    static class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private ProductAdapter adapter;

        SwipeToDeleteCallback(ProductAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder,
                              @NonNull final RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.deleteProduct(position);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + " Ft");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    void addProduct(Product product) {
        products.add(product);
        notifyItemInserted(products.size() - 1);
    }

    void deleteProduct(int position) {
        products.remove(position);
        notifyItemRemoved(position);
        sumUpdateCallback.execute();
    }

    void clearProducts() {
        products.clear();
        notifyDataSetChanged();
    }

    int getSum() {
        int sumPrice = 0;
        for (Product product : products) {
            sumPrice += product.getPrice();
        }
        return sumPrice;
    }
}
