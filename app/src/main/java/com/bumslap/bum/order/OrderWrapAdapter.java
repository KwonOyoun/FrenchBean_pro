package com.bumslap.bum.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by oyoun on 17. 12. 19.
 */

public class OrderWrapAdapter extends RecyclerView.Adapter<OrderWrapAdapter.OrderWrapAdapterViewHolder> {

    private ArrayList<OrderWrapDataSet> orderarrayList;
    private Context orderwrapcontext;

    private Context context = OrderActivity.context;
    AlertDialog.Builder PayCancelAlert;
    private int selectedPos = 0;
    String menuAmount, menuprice;
    int menutotalprice=0;
    String menutablenumber;

    OrderMenuSelectAdapter orderMenuSelectAdapter ;

    public OrderWrapAdapter(ArrayList<OrderWrapDataSet> orderarrayList, Context context) {
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
    public void onBindViewHolder(final OrderWrapAdapterViewHolder holder, final int position) {
       menutotalprice=0;

        holder.selectedCheck.setVisibility(selectedPos == position ? View.VISIBLE : View.INVISIBLE);



        final String billtitlenumber = orderarrayList.get(position).getBillTitleNumber();
        ArrayList billAllData = orderarrayList.get(position).getBillAllData();
        ArrayList<Order> aa = billAllData;
        for(int i = 0 ; i < billAllData.size(); i++){
            menuAmount = aa.get(i).getOrder_amount();
            menuprice = aa.get(i).getOrder_Price_perMenu();
            menutablenumber = aa.get(i).getOrder_Table_number();
            String transmenuAmount=menuAmount.replaceAll(",","");
            String transmenuprice = menuprice.replaceAll(",","");
            menutotalprice = menutotalprice+Integer.parseInt(transmenuAmount) * Integer.parseInt(transmenuprice);
        }
        holder.totalpayprice.setText(String.valueOf(menutotalprice));
        holder.orderbilltitlenumber.setText(billtitlenumber);
        holder.ordertablenumber.setText(menutablenumber);

        orderMenuSelectAdapter = new OrderMenuSelectAdapter(billAllData, orderwrapcontext);

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

                                        orderMenuSelectAdapter.saveItem();
                                        orderarrayList.remove(position);
                                        notifyDataSetChanged();



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
                                        //삭제시 진행될 행동.
                                        String cancelfor = "cancel";
                                        Intent intent = new Intent("custom-message");
                                        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                                        intent.putExtra("quantity",cancelfor);

                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

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


    class OrderWrapAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // public RecyclerView selectRecyclerView;
        public RecyclerView orderbilllistrecyclerView;
        public TextView orderbilltitlenumber;
        //public CardView orderbillcardview;
        public Button orderCancelBtn;
        public Button orderPayBtn;
        public TextView totalpayprice;
        public TextView ordertablenumber;
        ImageView selectedCheck;



        public OrderWrapAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            selectedCheck = view.findViewById(R.id.selectedCheck);
            orderbilltitlenumber = (TextView) view.findViewById(R.id.BillNumber);
            ordertablenumber = (TextView) view.findViewById(R.id.Tablenumber);
            orderCancelBtn = (Button) view.findViewById(R.id.cancelBTN);
            orderPayBtn = (Button) view.findViewById(R.id.payBTN);
            orderbilllistrecyclerView = (RecyclerView) view.findViewById(R.id.Bill_order_list);
            totalpayprice = (TextView)view.findViewById(R.id.totalpayprice);

        }

        @Override
        public void onClick(View view) {

            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
           // if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
            notifyDataSetChanged();
            // Do your another stuff for your onClick

        }
    }

}