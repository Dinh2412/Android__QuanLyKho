package com.example.quanlykhoraucu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykhoraucu.R;
import com.example.quanlykhoraucu.data.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactionList = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lưu ý: Đảm bảo layout item_transaction đã được tạo
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction currentTransaction = transactionList.get(position);
        boolean isImport = currentTransaction.transType.equals("NHAP");

        // --- A. Xử lý Loại giao dịch và Màu sắc ---
        holder.tvType.setText(isImport ? "NHẬP KHO" : "XUẤT KHO");

        // Đổi màu (Xanh lá cho Nhập, Đỏ cho Xuất)
        int color = isImport ?
                holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark) :
                holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
        holder.tvType.setTextColor(color);

        // --- B. Tính toán và Hiển thị Giá/Tổng tiền ---

        // Tính toán Tổng tiền
        double totalPrice = currentTransaction.unitPrice * currentTransaction.quantity;

        // Xác định nhãn động cho Giá đơn vị
        String priceLabel = isImport ? "Giá nhập: " : "Giá xuất: ";

        // 1. Hiển thị Số lượng và Giá đơn vị trên cùng một dòng (tvQty)
        holder.tvQty.setText(String.format("SL: %d | %s %,.0f đ",
                currentTransaction.quantity,
                priceLabel,
                currentTransaction.unitPrice));

        // 2. Hiển thị Tổng số tiền giao dịch trên dòng riêng (tvPrice)
        holder.tvPrice.setText(String.format("Tổng tiền: %,.0f đ", totalPrice));

        // --- C. Hiển thị Ngày và Giờ giao dịch ---
        holder.tvDate.setText("Thời gian: " + dateFormat.format(new Date(currentTransaction.transDate)));
    }

    @Override
    public int getItemCount() {
        return transactionList == null ? 0 : transactionList.size();
    }

    // ViewHolder
    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        // Lưu ý: Cần đảm bảo các ID này khớp với file item_transaction.xml
        private final TextView tvType;
        private final TextView tvQty;
        private final TextView tvPrice;
        private final TextView tvDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các Views
            tvType = itemView.findViewById(R.id.tv_trans_type);
            tvQty = itemView.findViewById(R.id.tv_trans_qty);
            tvPrice = itemView.findViewById(R.id.tv_trans_price);
            tvDate = itemView.findViewById(R.id.tv_trans_date);
        }
    }
}