package com.example.quanlykhoraucu.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    /** Phương thức lấy lịch sử giao dịch cho một sản phẩm cụ thể */
    @Query("SELECT * FROM transactions WHERE product_id = :productId ORDER BY trans_date DESC")
    List<Transaction> getHistoryByProductId(int productId);
}