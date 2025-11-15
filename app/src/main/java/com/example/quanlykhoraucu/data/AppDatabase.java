package com.example.quanlykhoraucu.data;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// SỬA DÒNG NÀY: Tăng version lên 3
@Database(entities = {Product.class, Transaction.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract TransactionDao transactionDao(); // DAO cho Lịch sử Giao dịch

    // Singleton pattern
    private static volatile AppDatabase INSTANCE;

    // Phương thức chính để lấy/khởi tạo database
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "product_database")
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            // QUAN TRỌNG: Cho phép Room tái tạo database khi version tăng
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ----------------------------------------------------------------------------------
    // KHỞI TẠO DỮ LIỆU MẪU (SAMPLE DATA)
    // ----------------------------------------------------------------------------------

    // Callback để xử lý khi database được tạo lần đầu
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // AsyncTask để chèn dữ liệu mẫu vào database
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProductDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.productDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Chèn dữ liệu mẫu (SỬA CONSTRUCTOR ĐỂ THÊM GIÁ VỐN)
            // Constructor mới: (tên, danh mục, đơn vị, giá bán, GIÁ VỐN, tồn kho)

            // --- Dữ liệu MẪU: Trái Cây ---
            Product apple = new Product("Táo Fuji", "Trái Cây", "Kg", 55000.0, 45000.0, 30);
            mDao.insert(apple);
            Product banana = new Product("Chuối Tiêu", "Trái Cây", "Nải", 25000.0, 20000.0, 15);
            mDao.insert(banana);

            // --- Dữ liệu MẪU: Rau ---
            Product spinach = new Product("Rau Cải Xanh", "Rau", "Bó", 12000.0, 9000.0, 50);
            mDao.insert(spinach);

            // --- Dữ liệu MẪU: Củ Quả ---
            Product potato = new Product("Khoai Tây", "Củ Quả", "Kg", 20000.0, 15000.0, 100);
            mDao.insert(potato);

            // --- Dữ liệu MẪU: Gia Vị ---
            Product ginger = new Product("Gừng Tươi", "Gia Vị", "G", 400.0, 300.0, 2000);
            mDao.insert(ginger);

            return null;
        }
    }
}