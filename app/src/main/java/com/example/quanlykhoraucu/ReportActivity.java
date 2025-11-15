package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.quanlykhoraucu.data.AppDatabase;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;

public class ReportActivity extends AppCompatActivity {

    private TextView tvTotalInventoryValue;
    private AppDatabase db;
    // Định dạng số tiền (ví dụ: 125,000,000 VNĐ)
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,### VNĐ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvTotalInventoryValue = findViewById(R.id.tv_total_inventory_value);
        db = AppDatabase.getDatabase(this);

        loadReportData();
    }

    // Hàm tải dữ liệu báo cáo (chạy trên luồng nền)
    private void loadReportData() {
        // Sử dụng Executor để chạy truy vấn database trên luồng nền
        Executors.newSingleThreadExecutor().execute(() -> {

            // 1. Thực hiện truy vấn DAO để lấy tổng giá trị tồn kho
            double totalValue = db.productDao().getTotalInventoryValue();

            // 2. Cập nhật UI trên luồng chính
            runOnUiThread(() -> {
                String formattedValue = DECIMAL_FORMAT.format(totalValue);
                tvTotalInventoryValue.setText(formattedValue);
            });
        });
    }
}