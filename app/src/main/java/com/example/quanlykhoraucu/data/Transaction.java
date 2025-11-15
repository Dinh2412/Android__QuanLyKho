package com.example.quanlykhoraucu.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Tên bảng là transactions
@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId; // ID sản phẩm liên quan

    @ColumnInfo(name = "trans_type")
    public String transType; // Loại giao dịch: "NHAP" hoặc "XUAT"

    @ColumnInfo(name = "quantity")
    public int quantity; // Số lượng nhập/xuất

    @ColumnInfo(name = "unit_price") // Giá (giá vốn khi nhập, giá bán khi xuất)
    public double unitPrice;

    @ColumnInfo(name = "trans_date")
    public long transDate; // Thời gian giao dịch (timestamp)

    // Constructor để tạo đối tượng Transaction
    public Transaction(int productId, String transType, int quantity, double unitPrice, long transDate) {
        this.productId = productId;
        this.transType = transType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.transDate = transDate;
    }

    // [Tùy chọn] Cần thêm Getters và Setters tại đây nếu bạn dùng chúng
}