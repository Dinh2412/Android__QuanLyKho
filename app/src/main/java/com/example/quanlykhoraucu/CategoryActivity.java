package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent; // Cần thiết

import com.example.quanlykhoraucu.adapter.ProductAdapter;
import com.example.quanlykhoraucu.data.AppDatabase;
import com.example.quanlykhoraucu.data.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private String currentCategory;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // 1. Ánh xạ View
        tvTitle = findViewById(R.id.tv_category_title);
        recyclerView = findViewById(R.id.recycler_view_products);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_product);

        // 2. Nhận dữ liệu danh mục từ MainActivity
        if (getIntent().hasExtra(MainActivity.EXTRA_CATEGORY)) {
            currentCategory = getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY);
            tvTitle.setText(currentCategory);
        } else {
            currentCategory = "Tất Cả Sản Phẩm";
            tvTitle.setText(currentCategory);
        }

        // 3. Khởi tạo Database và RecyclerView
        db = AppDatabase.getDatabase(this);

        adapter = new ProductAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 4. Thiết lập sự kiện click cho các item (Mở màn hình Xem/Sửa)
        adapter.setOnItemClickListener(product -> {
            // Chuyển sang ProductDetailActivity và gửi ID sản phẩm
            Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });

        // 5. Thiết lập sự kiện click cho nút Thêm Mới (Mở màn hình Thêm Mới)
        fabAdd.setOnClickListener(v -> {
            // [CODE ĐÃ SỬA] Chuyển sang ProductDetailActivity ở chế độ Thêm Mới
            Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
            // Gửi tên danh mục để ProductDetailActivity biết sản phẩm mới thuộc loại nào
            intent.putExtra("CATEGORY_NAME", currentCategory);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 6. Tải dữ liệu từ database và hiển thị
        loadProductsByCategory();
    }

    private void loadProductsByCategory() {
        // Truy vấn database bằng DAO: Lấy sản phẩm theo danh mục hiện tại
        List<Product> products = db.productDao().getProductsByCategory(currentCategory);

        // Cập nhật Adapter để hiển thị danh sách mới
        if (products != null) {
            adapter.setProducts(products);
        }
    }
}