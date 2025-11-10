package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnFruits;
    private Button btnVegetables;
    private Button btnTubers;
    private Button btnSpices;

    // Khai báo một hằng số để gửi tên danh mục qua Intent
    public static final String EXTRA_CATEGORY = "com.example.quanlykhoraucu.CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các nút từ XML
        btnFruits = findViewById(R.id.btn_fruits);
        btnVegetables = findViewById(R.id.btn_vegetables);
        btnTubers = findViewById(R.id.btn_tubers);
        btnSpices = findViewById(R.id.btn_spices);

        // Thiết lập sự kiện click cho các nút
        btnFruits.setOnClickListener(v -> launchCategoryActivity("Trái Cây"));
        btnVegetables.setOnClickListener(v -> launchCategoryActivity("Rau"));
        btnTubers.setOnClickListener(v -> launchCategoryActivity("Củ Quả"));
        btnSpices.setOnClickListener(v -> launchCategoryActivity("Gia Vị"));
    }

    // Phương thức chung để khởi động CategoryActivity và gửi tên danh mục
    private void launchCategoryActivity(String category) {
        // Lỗi sẽ xuất hiện ở dòng dưới cho đến khi CategoryActivity được tạo ở Bước 4
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        startActivity(intent);
    }
}