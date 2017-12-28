package com.bumslap.bum.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by oyoun on 17. 12. 19.
 */

public class OrderWrapAdapter extends RecyclerView.Adapter<OrderWrapAdapterViewHolder>{

    private ArrayList<OrderWrapDataSet> orderarrayList;
    private Context orderwrapcontext;
    private int Billnumberposition;
    private Context context = OrderActivity.context;
    AlertDialog.Builder PayCancelAlert;
    String menuAmount, menuprice;
    int menutotalprice=0;
    String menutablenumber;

    public OrderWrapAdapter(ArrayList<OrderWrapDataSet> orderarrayList, Context context){
        this.orderarrayList = orderarrayList;
        this.orderwrapcontext = context;
    }

    @Override
    public OrderWrapAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_bills_layout, parent, false);
        final OrderWrapAdapterViewHolder viewHolder = new OrderWrapAdapterViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderWrapAdapterViewHolder holder, int position) {
        menutotalprice=0;
        final String billtitlenumber = orderarrayList.get(position).getBillTitleNumber();
        ArrayList billAllData = orderarrayList.get(position).getBillAllData();
        ArrayList<Order> aa = billAllData;
        for(int i = 0 ; i < billAllData.size(); i++){
            menuAmount = aa.get(i).getOrder_amount();
            menuprice = aa.get(i).getOrder_Price_perMenu();
            menutablenumber = aa.get(i).getOrder_Table_number();
            menutotalprice = menutotalprice+Integer.parseInt(menuAmount) * Integer.parseInt(menuprice);
        }
        holder.totalpayprice.setText(String.valueOf(menutotalprice));
        holder.orderbilltitlenumber.setText(billtitlenumber);
        holder.ordertablenumber.setText(menutablenumber);

        OrderMenuSelectAdapter orderMenuSelectAdapter = new OrderMenuSelectAdapter(billAllData, orderwrapcontext);

        holder.orderbilllistrecyclerView.setLayoutManager(new LinearLayoutManager(orderwrapcontext, LinearLayoutManager.VERTICAL, false));
        holder.orderbilllistrecyclerView.setAdapter(orderMenuSelectAdapter);

        holder.orderPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PayCancelAlert = new AlertDialog.Builder(context);
                PayCancelAlert.setTitle("결재");
                PayCancelAlert
                        .setMessage("결재를 진행 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();
                                    }
                                })
                        .setNegativeButton("결재",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //결재시 진행될 행동.
                                        String payfor = "pay";
                                        Intent intent = new Intent("custom-message");
                                        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                                        intent.putExtra("quantity",payfor);

                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                        }

                                });
                AlertDialog alertDialog = PayCancelAlert.create();
                alertDialog.show();

            }
        });

        holder.orderCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PayCancelAlert = new AlertDialog.Builder(context);
                PayCancelAlert.setTitle("취소");
                PayCancelAlert
                        .setMessage("주문을 삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();
                                    }
                                })
                        .setNegativeButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //결재시 진행될 행동.

                                    }
                                });
                AlertDialog alertDialog = PayCancelAlert.create();
                alertDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {

        //다시 봐야 할 필요가 있는 부분.

        return (null != orderarrayList ? orderarrayList.size() : 0);
    }
}

class OrderWrapAdapterViewHolder extends RecyclerView.ViewHolder{
   // public RecyclerView selectRecyclerView;
    public RecyclerView orderbilllistrecyclerView;
    public TextView orderbilltitlenumber;
    //public CardView orderbillcardview;
    public Button orderCancelBtn;
    public Button orderPayBtn;
    public TextView totalpayprice;
    public TextView ordertablenumber;

    public ConstraintLayout billcon;
    int billnumberposition=0;
    Order order;
    int selectLength;
    long CurrentTimeCall;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    SimpleDateFormat CurrentTime;
    String CurrentTimes, CurrentDates;
    DBforAnalysis newdbforAnalysis;


    public OrderWrapAdapterViewHolder(View view){

        super(view);
        //orderbillcardview = (CardView)view.findViewById(R.id.order_bill_cardview);
        ordertablenumber = (TextView)view.findViewById(R.id.Tablenumber);
        orderbilltitlenumber = (TextView)view.findViewById(R.id.BillNumber);
        orderCancelBtn = (Button) view.findViewById(R.id.cancelBTN);
        orderPayBtn = (Button) view.findViewById(R.id.payBTN);
        totalpayprice = (TextView)view.findViewById(R.id.totalpayprice);


        orderbilllistrecyclerView = (RecyclerView)view.findViewById(R.id.Bill_order_list);


    }
}