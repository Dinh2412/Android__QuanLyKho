package com.example.quanlykhoraucu.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "unit")
    public String unit;

    @ColumnInfo(name = "price")
    public double price; // Giá Bán

    @ColumnInfo(name = "cost_price") // TRƯỜNG MỚI: Giá Vốn/Giá Nhập
    public double costPrice;

    @ColumnInfo(name = "inventory_qty")
    public int inventoryQty;

    // Cập nhật Constructor để bao gồm trường costPrice
    public Product(String name, String category, String unit, double price, double costPrice, int inventoryQty) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.price = price;
        this.costPrice = costPrice; // Gán giá trị Giá Vốn
        this.inventoryQty = inventoryQty;
    }

    // --- Getters và Setters (Bắt buộc phải có để Room truy cập dữ liệu) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getCostPrice() { return costPrice; } // Getter cho Giá Vốn
    public void setCostPrice(double costPrice) { this.costPrice = costPrice; } // Setter cho Giá Vốn
    public int getInventoryQty() { return inventoryQty; }
    public void setInventoryQty(int inventoryQty) { this.inventoryQty = inventoryQty; }
}