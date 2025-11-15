package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhoraucu.adapter.TransactionAdapter;
import com.example.quanlykhoraucu.data.AppDatabase;
import com.example.quanlykhoraucu.data.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private AppDatabase db;
    private int productId;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        db = AppDatabase.getDatabase(this);
        tvTitle = findViewById(R.id.tv_history_title);

        // 1. Nhận ID và Tên sản phẩm được gửi từ ProductDetailActivity
        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            String productName = getIntent().getStringExtra("PRODUCT_NAME");
            tvTitle.setText("Lịch Sử Giao Dịch: " + productName);
        } else {
            Toast.makeText(this, "Không tìm thấy ID sản phẩm.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 2. Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recycler_view_transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo Adapter với danh sách rỗng
        adapter = new TransactionAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 3. Tải lịch sử giao dịch
        if (productId != -1) {
            loadTransactionHistory(productId);
        }
    }

    private void loadTransactionHistory(int id) {
        // Chạy truy vấn trong luồng nền vì thao tác DB
        new Thread(() -> {
            // Lấy lịch sử bằng TransactionDao (Mới nhất trước)
            List<Transaction> history = db.transactionDao().getHistoryByProductId(id);

            // Cập nhật giao diện trên luồng chính
            runOnUiThread(() -> {
                if (history != null && !history.isEmpty()) {
                    adapter.setTransactions(history);
                } else {
                    tvTitle.append(" (Chưa có giao dịch nào)");
                }
            });
        }).start();
    }
}