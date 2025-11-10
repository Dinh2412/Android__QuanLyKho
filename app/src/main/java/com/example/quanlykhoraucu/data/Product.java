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
    public double price;

    @ColumnInfo(name = "inventory_qty")
    public int inventoryQty;

    public Product(String name, String category, String unit, double price, int inventoryQty) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.price = price;
        this.inventoryQty = inventoryQty;
    }

    // Cần có Getters và Setters để tương tác với data
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }

}