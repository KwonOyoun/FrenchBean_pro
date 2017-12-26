package com.bumslap.bum.statistics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumslap.bum.DB.DBHelper;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class SalesStatus2Activity extends AppCompatActivity {
    DBProvider db;
    ArrayList<HashMap<String, Integer>> OrderList;
    HashMap<String, Integer> Ordermap;
    ArrayList<Order> Order_menu_List;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    String CurrentTimes, CurrentDates;
    int Order_Amount;
    Order putOrder;
    public static DBHelper dbforAnalysis;
    public DBforAnalysis newdbforAnalysis;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesstatus_goh);

        Order_menu_List = new ArrayList<Order>();
        db = new DBProvider(this);
        db.open();
        newdbforAnalysis = new DBforAnalysis(this);
        // putOrder이 코스트 DB
        try {
            Cursor cursor = db.getData("SELECT * FROM COST_TABLE");
            Order_menu_List.clear();
            while (cursor.moveToNext()) {
                String amount = cursor.getString(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String FK_menuId = cursor.getString(3);
                String number = cursor.getString(4);
                String Price_perMenu = cursor.getString(4);

                Order_menu_List.add(new com.bumslap.bum.DB.Order(amount, date, time, FK_menuId, number,Price_perMenu));
            }



        }catch (Exception e){}





    }
}
