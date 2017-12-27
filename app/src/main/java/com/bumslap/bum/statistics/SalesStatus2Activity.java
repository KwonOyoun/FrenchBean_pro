package com.bumslap.bum.statistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import com.bumslap.bum.DB.Cost;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.R;
import com.bumslap.bum.menuedit.CostSettingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class SalesStatus2Activity extends AppCompatActivity {
    DBProvider db;
    ArrayList<HashMap<String, Integer>> OrderList;
    ArrayList<Order> Order_date_List;
    ArrayList<Menu> menu_id_cost;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    String CurrentTimes, CurrentDates;
    DBforAnalysis newdbforAnalysis;
    String date = "2017-12-26";
    TextView sumDate;
    TextView sumCost;
    String Menu_id;
    String Menu_amount;
    String Order_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_status2);

        Order_date_List = new ArrayList<>();
        db = new DBProvider(this);
        db.open();
        newdbforAnalysis = new DBforAnalysis(this);
        //하루 총판매금액과 원가 표시창
        sumDate = (TextView)findViewById(R.id.sumdate);
        sumCost = (TextView)findViewById(R.id.sumcost);

        // DBforAnalysis 주문 테이블 가져오기
        Order_date_List = newdbforAnalysis.getOrdersMatchDateData(date);

/*
for(){
    for (int i = 0; i < Order_date_List.size(); i++) {
        ArrayList<String> ordermenuid = new ArrayList<>();
        String id = Order_date_List.get(i).getOrder_FK_menuId();
        if (id ==)
    }
}
*/

        //Menu_id로 oredr 원가 가져오기
        //menu_id_cost = newdbforAnalysis.getMenuCost();


        int SalesTotal = 0;
        for(int k=0; k<Order_date_List.size(); k++){
            if(isNumber(Order_date_List.get(k).getOrder_Price_perMenu()))
                SalesTotal = SalesTotal + Integer.parseInt(Order_date_List.get(k).getOrder_Price_perMenu());
        }
        sumDate.setText(String.valueOf(SalesTotal)+"원");


        int CostTotal = 0;
        for(int k=0; k<Order_date_List.size(); k++){
            if(isNumber(Order_date_List.get(k).getOrder_Price_perMenu()))
                SalesTotal = SalesTotal + Integer.parseInt(Order_date_List.get(k).getOrder_Price_perMenu());
        }
        sumDate.setText(String.valueOf(SalesTotal)+"원");



    }


    public static boolean isNumber(String str){
        boolean result = false;
        try{
            Double.parseDouble(str) ;
            result = true ;
        }catch(Exception e){}
        return result ;
    }
}
