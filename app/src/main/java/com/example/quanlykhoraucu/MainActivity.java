package com.example.quanlykhoraucu;



import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;



public class MainActivity extends AppCompatActivity {



    private Button btnFruits;

    private Button btnVegetables;

    private Button btnTubers;

    private Button btnSpices;

    private Button btnViewReport; // Thêm nút Báo cáo

    private SearchView searchView;



    // Khai báo các hằng số Intent

    public static final String EXTRA_CATEGORY = "com.example.quanlykhoraucu.CATEGORY";

    public static final String EXTRA_SEARCH_QUERY = "SEARCH_QUERY"; // Hằng số cho truy vấn tìm kiếm



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        // 1. Ánh xạ các Views

        btnFruits = findViewById(R.id.btn_fruits);

        btnVegetables = findViewById(R.id.btn_vegetables);

        btnTubers = findViewById(R.id.btn_tubers);

        btnSpices = findViewById(R.id.btn_spices);

        btnViewReport = findViewById(R.id.btn_view_report); // Ánh xạ nút Báo cáo

        searchView = findViewById(R.id.search_view_main);



        // 2. Thiết lập sự kiện click cho các nút Danh mục (Filter)

        btnFruits.setOnClickListener(v -> launchCategoryActivity("Trái Cây"));

        btnVegetables.setOnClickListener(v -> launchCategoryActivity("Rau"));

        btnTubers.setOnClickListener(v -> launchCategoryActivity("Củ Quả"));

        btnSpices.setOnClickListener(v -> launchCategoryActivity("Gia Vị"));



        // 3. Thiết lập sự kiện click cho nút Báo cáo

        btnViewReport.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ReportActivity.class);

            startActivity(intent);

        });



        // 4. THIẾT LẬP LISTENER CHO TÌM KIẾM

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {

                // Khi người dùng nhấn nút tìm kiếm (Submit)

                if (query != null && !query.isEmpty()) {

                    launchSearchActivity(query);

                }

                searchView.clearFocus();

                return true;

            }



            @Override

            public boolean onQueryTextChange(String newText) {

                return false;

            }

        });

    }



    // Phương thức chung để khởi động CategoryActivity cho Danh mục (Filter)

    private void launchCategoryActivity(String category) {

        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);

        intent.putExtra(EXTRA_CATEGORY, category);

        startActivity(intent);

    }



    // Phương thức riêng để khởi động CategoryActivity cho Tìm kiếm (Search)

    private void launchSearchActivity(String query) {

        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);

        // Gửi từ khóa tìm kiếm

        intent.putExtra(EXTRA_SEARCH_QUERY, query);

        startActivity(intent);

    }

}