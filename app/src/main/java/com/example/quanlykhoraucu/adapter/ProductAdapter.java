package com.example.quanlykhoraucu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykhoraucu.R;
import com.example.quanlykhoraucu.data.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener; // Đã khai báo

    // 1. Định nghĩa Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    // 2. Phương thức thiết lập Listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    // Phương thức cập nhật danh sách
    public void setProducts(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvName.setText(currentProduct.getName());
        holder.tvPrice.setText(String.format("%,.0f đ", currentProduct.price));
        holder.tvQty.setText("Tồn: " + currentProduct.inventoryQty + " " + currentProduct.unit);

        // 3. Xử lý sự kiện click (Gọi listener)
        holder.itemView.setOnClickListener(v -> { // Cú pháp Lambda
            if (listener != null) {
                listener.onItemClick(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    // ViewHolder: Ánh xạ các thành phần giao diện
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvQty;
        private final TextView tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvQty = itemView.findViewById(R.id.tv_inventory_qty);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}