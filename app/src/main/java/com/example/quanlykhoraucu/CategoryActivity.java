package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
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
    private String searchQuery = null; // KHAI BÁO BIẾN MỚI cho truy vấn tìm kiếm
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // 1. Ánh xạ View
        tvTitle = findViewById(R.id.tv_category_title);
        recyclerView = findViewById(R.id.recycler_view_products);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_product);

        // 2. Nhận dữ liệu danh mục HOẶC từ khóa tìm kiếm
        if (getIntent().hasExtra(MainActivity.EXTRA_CATEGORY)) {
            // Trường hợp lọc theo 4 nút danh mục
            currentCategory = getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY);
            tvTitle.setText(currentCategory);
        } else if (getIntent().hasExtra(MainActivity.EXTRA_SEARCH_QUERY)) {
            // TRƯỜNG HỢP TÌM KIẾM
            searchQuery = getIntent().getStringExtra(MainActivity.EXTRA_SEARCH_QUERY);
            tvTitle.setText("Kết quả tìm kiếm: \"" + searchQuery + "\"");

            // Ẩn nút Thêm mới khi đang hiển thị kết quả tìm kiếm chung
            fabAdd.setVisibility(View.GONE);
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
            Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });

        // 5. Thiết lập sự kiện click cho nút Thêm Mới
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang ProductDetailActivity ở chế độ Thêm Mới
            Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
            intent.putExtra("CATEGORY_NAME", currentCategory);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 6. Tải dữ liệu từ database và hiển thị
        loadProducts(); // GỌI HÀM TẢI SẢN PHẨM MỚI
    }

    // HÀM TẢI DỮ LIỆU ĐÃ ĐƯỢC CẬP NHẬT ĐỂ HỖ TRỢ TÌM KIẾM
    private void loadProducts() {
        List<Product> products;

        if (searchQuery != null) {
            // THỰC HIỆN TÌM KIẾM: Dùng dấu % để tìm kiếm linh hoạt trong SQL
            products = db.productDao().searchProducts("%" + searchQuery + "%");
        } else {
            // Lọc theo danh mục (chế độ bình thường)
            products = db.productDao().getProductsByCategory(currentCategory);
        }

        // Cập nhật Adapter
        if (products != null) {
            adapter.setProducts(products);
        }
    }
}