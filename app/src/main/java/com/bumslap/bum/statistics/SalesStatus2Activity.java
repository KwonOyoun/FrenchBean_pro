package com.bumslap.bum.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.DB.Menu;
import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SalesStatus2Activity extends AppCompatActivity {
    DBProvider db;
    ArrayList<Order> Order_date_List;
    ArrayList<Menu> menu_id_cost;
    DBforAnalysis newdbforAnalysis;
    String date = "2017-12-26";
    TextView sumDate,sumCost,profit;
    ImageButton mainback;
    int CostTotal = 0;
    Integer SalesTotal = 0;
    int marginTotal=0;

    long CurrentTimeCall;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    String CurrentDates;
    String price;

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
        profit = (TextView)findViewById(R.id.profit);
        mainback= (ImageButton)findViewById(R.id.mainback);

        //현재 날짜 가져오기
        CurrentTimeCall = System.currentTimeMillis();
        CurrentDateCall = new Date(CurrentTimeCall);
        CurrentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        CurrentDates = CurrentDate.format(CurrentDateCall);

        // DBforAnalysis 주문 테이블 가져오기
        Order_date_List = newdbforAnalysis.getOrdersMatchDateData(CurrentDates);
        menu_id_cost = newdbforAnalysis.getMenuIdCost();

        //메뉴id와 오더id 비교 후 총비용 계산.
        for(int k=0; k < menu_id_cost.size(); k++){

            for (int i=0; i < Order_date_List.size(); i++) {

            String menuid = menu_id_cost.get(k).getMenu_id();
            String orderid = Order_date_List.get(i).getOrder_FK_menuId();
                if (menuid.equals(orderid)){

                 int amount = Integer.parseInt(Order_date_List.get(i).getOrder_amount());
                 int cost =Integer.parseInt(menu_id_cost.get(k).getMenu_cost());

                 int originprice = amount * cost;
                 CostTotal = originprice ++;

                    }
                }
            }

        sumCost.setText(String.valueOf(CostTotal)+"원");

        //날짜로 불러온 주문 총매출액 가져오기

        for(int k=0; k<Order_date_List.size(); k++){
            if(isNumber(Order_date_List.get(k).getOrder_Price_perMenu()))
               price = Order_date_List.get(k).getOrder_Price_perMenu();
               String noneprice = price.replaceAll(",","");
                SalesTotal = SalesTotal + Integer.parseInt(noneprice);
            }

        sumDate.setText(String.valueOf(SalesTotal)+"원");


        //순이익 구하기
        marginTotal = SalesTotal- CostTotal;
        profit.setText(String.valueOf(marginTotal)+"원");

        //메인으로 가지
        mainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }


    public static boolean isNumber(String str){
       String nonestr = str.replaceAll(",","");
        boolean result = false;
        try{
            Double.parseDouble(nonestr) ;
            result = true ;
        }catch(Exception e){}
        return result ;
    }
}

