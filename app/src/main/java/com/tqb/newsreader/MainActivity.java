package com.tqb.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.tqb.newsreader.backend.Controller;
import com.tqb.newsreader.backend.DatabaseHandler;
import com.tqb.newsreader.backend.thanhnien.ThanhNien;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sử dụng database bằng database handler
        DatabaseHandler db = new DatabaseHandler(this);

        db.onCreate(db.getWritableDatabase()); //Khởi tạo database (Không cần gọi lại)
        db.clearNews();

        //Tạo controller để sử dụng
        Controller controller = new Controller(this);
        controller.latest(); //Lấy tin mới nhất
        controller.baseOnFavorite(); //Lấy tin mới dựa vào các thể loại yêu thích
        //Sau mỗi lần gọi hàm, database sẽ tự động xóa và import lại tin tức
        
        //Sử dụng database bằng cách truy vấn trực tiếp
    }
}