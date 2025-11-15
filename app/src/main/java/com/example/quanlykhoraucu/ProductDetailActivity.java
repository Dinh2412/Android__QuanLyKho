package com.example.quanlykhoraucu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.example.quanlykhoraucu.data.AppDatabase;
import com.example.quanlykhoraucu.data.Product;
import com.example.quanlykhoraucu.data.Transaction;

public class ProductDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private Product currentProduct;
    private int productId = -1;

    // Khai báo các View (ĐÃ THÊM etCostPrice)
    private EditText etName, etPrice, etTransQty, etCostPrice;
    private AutoCompleteTextView actvUnit;
    private TextView tvCurrentQty, tvTitle;
    private Button btnImport, btnExport, btnSave, btnDelete, btnViewHistory;

    private static final String[] UNITS = new String[] {"Kg", "Gram", "Bó", "Củ", "Nải", "Quả"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getDatabase(this);

        // 1. Ánh xạ Views
        tvTitle = findViewById(R.id.tv_detail_title);
        etName = findViewById(R.id.et_product_name);
        etPrice = findViewById(R.id.et_price);
        etCostPrice = findViewById(R.id.et_cost_price); // ÁNH XẠ GIÁ VỐN
        etTransQty = findViewById(R.id.et_trans_qty);
        actvUnit = findViewById(R.id.actv_unit);
        tvCurrentQty = findViewById(R.id.tv_current_qty);
        btnImport = findViewById(R.id.btn_import);
        btnExport = findViewById(R.id.btn_export);
        btnSave = findViewById(R.id.btn_save_changes);
        btnDelete = findViewById(R.id.btn_delete_product);
        btnViewHistory = findViewById(R.id.btn_view_history);

        // Thiết lập Adapter cho AutoCompleteTextView (Đơn vị)
        ArrayAdapter<String> adapterUnit = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, UNITS);
        actvUnit.setAdapter(adapterUnit);

        // 2. Kiểm tra chế độ Thêm mới hay Xem/Sửa
        if (getIntent().hasExtra("PRODUCT_ID")) {
            // CHẾ ĐỘ XEM/SỬA
            productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            loadProductData(productId);

            // Listener cho Xem/Sửa/Nhập/Xuất
            btnSave.setOnClickListener(v -> saveChanges(false));
            btnDelete.setOnClickListener(v -> deleteProduct());
            btnImport.setOnClickListener(v -> performTransaction(true));
            btnExport.setOnClickListener(v -> performTransaction(false));

            // Listener Xem Lịch Sử
            btnViewHistory.setOnClickListener(v -> {
                Intent intent = new Intent(ProductDetailActivity.this, TransactionHistoryActivity.class);
                intent.putExtra("PRODUCT_ID", productId);
                intent.putExtra("PRODUCT_NAME", currentProduct.getName());
                startActivity(intent);
            });

        } else if (getIntent().hasExtra("CATEGORY_NAME")) {
            // CHẾ ĐỘ THÊM MỚI
            String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
            tvTitle.setText("Thêm Sản Phẩm Mới (" + categoryName + ")");

            // Khởi tạo đối tượng sản phẩm mới: (name, category, unit, price, costPrice, inventoryQty)
            currentProduct = new Product("", categoryName, "", 0.0, 0.0, 0);

            // Ẩn các chức năng quản lý kho/xóa khi thêm mới
            btnImport.setVisibility(View.GONE);
            btnExport.setVisibility(View.GONE);
            tvCurrentQty.setVisibility(View.GONE);
            etTransQty.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnViewHistory.setVisibility(View.GONE);

            // Listener cho nút Lưu (thực hiện Thêm mới)
            btnSave.setOnClickListener(v -> saveChanges(true));
        }
    }

    // Hàm tải dữ liệu (ĐÃ CẬP NHẬT TẢI GIÁ VỐN)
    private void loadProductData(int id) {
        currentProduct = db.productDao().getProductById(id);

        if (currentProduct != null) {
            tvTitle.setText("Quản lý: " + currentProduct.getName());
            etName.setText(currentProduct.getName());
            etPrice.setText(String.valueOf(currentProduct.price));
            etCostPrice.setText(String.valueOf(currentProduct.costPrice)); // TẢI GIÁ VỐN
            actvUnit.setText(currentProduct.unit, false);
            updateInventoryDisplay();
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Hàm cập nhật hiển thị tồn kho
    private void updateInventoryDisplay() {
        if (currentProduct != null) {
            tvCurrentQty.setText("Tồn Kho: " + currentProduct.inventoryQty + " " + currentProduct.unit);
        }
    }

    // Hàm xử lý Nhập/Xuất Kho (ĐÃ CẬP NHẬT DÙNG GIÁ VỐN/GIÁ BÁN)
    private void performTransaction(boolean isImport) {
        String qtyText = etTransQty.getText().toString();
        if (qtyText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);

            // XÁC ĐỊNH GIÁ GIAO DỊCH: Giá Vốn khi nhập, Giá Bán khi xuất
            double transactionPrice = isImport ? currentProduct.costPrice : currentProduct.price;

            if (isImport) {
                // Nhập kho
                currentProduct.inventoryQty += quantity;
                recordTransaction(quantity, "NHAP", transactionPrice);
                Toast.makeText(this, "Nhập kho thành công: +" + quantity, Toast.LENGTH_SHORT).show();
            } else {
                // Xuất kho (Kiểm tra tồn kho)
                if (currentProduct.inventoryQty >= quantity) {
                    currentProduct.inventoryQty -= quantity;
                    recordTransaction(quantity, "XUAT", transactionPrice);
                    Toast.makeText(this, "Xuất kho thành công: -" + quantity, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi: Số lượng xuất vượt quá tồn kho!", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            saveChanges(false); // Cập nhật (update) thay đổi tồn kho
            etTransQty.setText("");
            updateInventoryDisplay();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ.", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm GHI LẠI LỊCH SỬ GIAO DỊCH
    private void recordTransaction(int quantity, String type, double price) {
        Transaction transaction = new Transaction(
                currentProduct.getId(),
                type,
                quantity,
                price,
                System.currentTimeMillis()
        );
        new Thread(() -> {
            db.transactionDao().insert(transaction);
        }).start();
    }


    // Hàm Lưu thay đổi hoặc Thêm mới (ĐÃ CẬP NHẬT LƯU GIÁ VỐN)
    private void saveChanges(boolean isNew) {
        // Kiểm tra dữ liệu chung
        String newName = etName.getText().toString();
        String newUnit = actvUnit.getText().toString();
        double newPrice = 0.0;
        double newCostPrice = 0.0; // Biến mới

        if (newName.isEmpty() || newUnit.isEmpty() || etPrice.getText().toString().isEmpty() || etCostPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            newPrice = Double.parseDouble(etPrice.getText().toString());
            newCostPrice = Double.parseDouble(etCostPrice.getText().toString()); // LẤY GIÁ VỐN
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá bán hoặc Giá vốn không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật đối tượng hiện tại
        currentProduct.name = newName;
        currentProduct.unit = newUnit;
        currentProduct.price = newPrice;
        currentProduct.costPrice = newCostPrice; // CẬP NHẬT GIÁ VỐN

        if (isNew) {
            db.productDao().insert(currentProduct);
            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            db.productDao().update(currentProduct);
            Toast.makeText(this, "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show();
            updateInventoryDisplay();
        }
    }

    // Hàm Xóa sản phẩm
    private void deleteProduct() {
        db.productDao().deleteById(currentProduct.getId());
        Toast.makeText(this, currentProduct.getName() + " đã bị xóa khỏi kho.", Toast.LENGTH_SHORT).show();
        finish();
    }
}