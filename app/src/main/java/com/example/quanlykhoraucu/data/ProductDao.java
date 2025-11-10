package com.example.quanlykhoraucu.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {

    /**
     * Thêm một sản phẩm mới vào database.
     */
    @Insert
    void insert(Product product);

    /**
     * Cập nhật thông tin của một sản phẩm đã có (Dùng cho Sửa thông tin và Nhập/Xuất kho).
     */
    @Update
    void update(Product product);

    /**
     * Xóa sản phẩm theo ID.
     */
    @Query("DELETE FROM products WHERE id = :productId")
    void deleteById(int productId);

    /**
     * Lấy tất cả sản phẩm, sắp xếp theo tên.
     */
    @Query("SELECT * FROM products ORDER BY name ASC")
    List<Product> getAllProducts();

    /**
     * Lấy danh sách sản phẩm theo Danh mục (dùng cho CategoryActivity).
     */
    @Query("SELECT * FROM products WHERE category = :categoryName ORDER BY name ASC")
    List<Product> getProductsByCategory(String categoryName);

    /**
     * Tìm kiếm sản phẩm theo tên (dùng cho SearchView trên MainActivity).
     * Lưu ý: Cần truyền vào định dạng tìm kiếm của SQL (ví dụ: "%Táo%").
     */
    @Query("SELECT * FROM products WHERE name LIKE :searchQuery")
    List<Product> searchProducts(String searchQuery);

    /**
     * Lấy một sản phẩm duy nhất theo ID (dùng cho ProductDetailActivity).
     */
    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductById(int productId);
}