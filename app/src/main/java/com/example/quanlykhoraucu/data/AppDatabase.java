package com.example.quanlykhoraucu.data;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// Đánh dấu lớp này là Room Database.
// entities là các bảng của database, version là phiên bản database.
@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Khai báo DAO (lớp để truy cập database)
    public abstract ProductDao productDao();

    // Singleton pattern (để đảm bảo chỉ có một instance database được tạo)
    private static volatile AppDatabase INSTANCE;

    // Phương thức chính để lấy/khởi tạo database
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "product_database") // Tên file database
                            .allowMainThreadQueries() // CẦN ĐỂ TEST NHANH, NÊN XÓA SAU NÀY
                            .addCallback(sRoomDatabaseCallback) // Thêm Callback để chèn dữ liệu mẫu
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
        /**
         * Được gọi khi database được tạo lần đầu (ví dụ: lần chạy ứng dụng đầu tiên).
         */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Thực thi AsyncTask để chèn dữ liệu nền
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // AsyncTask để chèn dữ liệu mẫu vào database trên luồng nền (background thread)
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProductDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.productDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Chèn dữ liệu mẫu

            // --- Dữ liệu MẪU: Trái Cây ---
            Product apple = new Product("Táo Fuji", "Trái Cây", "Kg", 55000.0, 30);
            mDao.insert(apple);
            Product banana = new Product("Chuối Tiêu", "Trái Cây", "Nải", 25000.0, 15);
            mDao.insert(banana);

            // --- Dữ liệu MẪU: Rau ---
            Product spinach = new Product("Rau Cải Xanh", "Rau", "Bó", 12000.0, 50);
            mDao.insert(spinach);

            // --- Dữ liệu MẪU: Củ Quả ---
            Product potato = new Product("Khoai Tây", "Củ Quả", "Kg", 20000.0, 100);
            mDao.insert(potato);

            // --- Dữ liệu MẪU: Gia Vị ---
            Product ginger = new Product("Gừng Tươi", "Gia Vị", "G", 400.0, 2000); // 400đ/g
            mDao.insert(ginger);

            return null;
        }
    }
}