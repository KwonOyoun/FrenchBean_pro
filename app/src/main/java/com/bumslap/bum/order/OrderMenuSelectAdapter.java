package com.bumslap.bum.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.DB.DBHelper;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by oyoun on 17. 12. 18.
 */

public class OrderMenuSelectAdapter extends RecyclerView.Adapter<OrderMenuViewHoler> {
    private ArrayList<Order> Menuitems = new ArrayList<>();
    private Context context;
    private Context contextbro = OrderActivity.context;
    DBforAnalysis dBforAnalysis;
    String MenunameDB;
    int i, j, k = 0;

    long CurrentTimeCall;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    SimpleDateFormat CurrentTime;
    String CurrentTimes, CurrentDates;
    int Order_Amount;
    Order putOrder;
    public DBforAnalysis newdbforAnalysis;
    DBProvider db;

    String getordermenuamount;
    String getordermenuid;
    String getordermenuprice;

    int currentTotalgain;
    String payfor;
    Intent intent;
    String detailfor;
    String totalgain;
    public OrderMenuSelectAdapter(ArrayList<Order> orderArrayList, Context context) {
        try {
            k = orderArrayList.size();
            for (i = 0; i < orderArrayList.size(); i++) {
                for (j = 0; j <= i; j++) {
                    if (Integer.parseInt(orderArrayList.get(i).getOrder_FK_menuId()) == Integer.parseInt(orderArrayList.get(j).getOrder_FK_menuId())) {
                        orderArrayList.set(j, orderArrayList.get(i));

                        k++;
                        if (k == orderArrayList.size() * 2 + 1) {
                            orderArrayList.remove(orderArrayList.size() - 1);
                        }
                    }
                }
            }
        } catch (Exception ec) {

        }


        this.Menuitems = orderArrayList;
        this.context = context;
    }

    @Override
    public OrderMenuViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.order_bill_item, parent, false);


        return new OrderMenuViewHoler(v);
    }
    public void cancelItem(){
        notifyDataSetChanged();
        payfor = "cancel";

        Intent intent = new Intent("custom-message");

        intent.putExtra("quantity",payfor);

        LocalBroadcastManager.getInstance(contextbro).sendBroadcast(intent);
    }

    public void saveItem(){
        Menuitems.size();
        db = new DBProvider(context);
        db.open();

        newdbforAnalysis = new DBforAnalysis(context);

        for (int i = 0 ; i < Menuitems.size() ; i++){
            getordermenuprice = Menuitems.get(i).getOrder_Price_perMenu();
            getordermenuamount = Menuitems.get(i).getOrder_amount();
            getordermenuid = Menuitems.get(i).getOrder_FK_menuId();

            CurrentTimeCall = System.currentTimeMillis();
            CurrentDateCall = new Date(CurrentTimeCall);
            CurrentDate = new SimpleDateFormat("yyyy-MM-dd");
            CurrentTime = new SimpleDateFormat("kk-mm-ss");
            CurrentDates = CurrentDate.format(CurrentDateCall);
            CurrentTimes = CurrentTime.format(CurrentDateCall);

            putOrder = new Order();
            putOrder.setOrder_FK_menuId(getordermenuid);
            putOrder.setOrder_amount(getordermenuamount);
            putOrder.setOrder_Price_perMenu(getordermenuprice);
            putOrder.setOrder_time(CurrentTimes);
            putOrder.setOrder_date(CurrentDates);

            newdbforAnalysis.addOrder(putOrder);
            currentTotalgain = currentTotalgain + Integer.parseInt(getordermenuamount) * Integer.parseInt(getordermenuprice);
        }

        Toast.makeText(OrderActivity.context, "결재 완료", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
        payfor = "pay";
        detailfor = "detail";
        totalgain = String.valueOf(currentTotalgain);


        Intent intent = new Intent("custom-message");

        intent.putExtra("quantity",payfor);
        intent.putExtra("detailvalue",detailfor);
        intent.putExtra("totalgain",totalgain);

        LocalBroadcastManager.getInstance(contextbro).sendBroadcast(intent);

    }




    @Override
    public void onBindViewHolder(final OrderMenuViewHoler holder, final int position) {
        dBforAnalysis = new DBforAnalysis(context);
        final Order menuitem = Menuitems.get(position);

        MenunameDB = dBforAnalysis.getMenuName(Integer.parseInt(menuitem.getOrder_FK_menuId()));

        holder.Menuname.setText(MenunameDB);
        holder.MenuId.setText(menuitem.getOrder_FK_menuId());
        holder.MenuAmount.setText(menuitem.getOrder_amount());

        holder.removeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(menuitem.getOrder_amount())!=0){
                    holder.MenuAmount.setText(String.valueOf(Integer.parseInt(String.valueOf(holder.MenuAmount.getText()))-1));
                    menuitem.setOrder_amount(String.valueOf(Integer.parseInt(String.valueOf(holder.MenuAmount.getText()))));
                    BroadcastingTotalValue();

                    notifyDataSetChanged();
                }

            }
        });



    }

    private void BroadcastingTotalValue() {
        for (int i = 0 ; i < Menuitems.size() ; i++){
            getordermenuamount = Menuitems.get(i).getOrder_amount();
            getordermenuprice = Menuitems.get(i).getOrder_Price_perMenu();
            currentTotalgain = currentTotalgain + Integer.parseInt(getordermenuamount) * Integer.parseInt(getordermenuprice);
        }
        payfor = "pay";
        intent = new Intent("custom-message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("quantity",payfor);
        intent.putExtra("currentTotalgain",String.valueOf(currentTotalgain));

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return (null != Menuitems ? Menuitems.size() : 0);
    }

}

class OrderMenuViewHoler extends RecyclerView.ViewHolder{

    public TextView Menuname, MenuAmount, MenuId;
    public ImageView removeBTN;


    public OrderMenuViewHoler(View OrderitemView){
        super(OrderitemView);

        removeBTN = (ImageView)OrderitemView.findViewById(R.id.removeBTN);
        MenuId = (TextView)OrderitemView.findViewById(R.id.ordermenuID);
        Menuname = (TextView)OrderitemView.findViewById(R.id.ordermenuname);
        MenuAmount = (TextView)OrderitemView.findViewById(R.id.ordermenuamount);


    }

}