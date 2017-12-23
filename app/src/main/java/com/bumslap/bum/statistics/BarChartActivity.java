package com.bumslap.bum.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BarChartActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    BarChart chart;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;
    XAxis xAxis;
    long a, b, c, d, e, f, g, h, i, j, k, l;
    int[] color = {Color.rgb(185, 193, 144)};
    private GestureDetector gestureDetector;
    Intent mvStaIntent;
    Button AmountStastisticBtn, SalesStatisticBtn;
    ArrayList<Integer> a_list, b_list, c_list, d_list, e_list, f_list,
            g_list, h_list, i_list, j_list, k_list, l_list = null;
    ArrayList<ArrayList<Integer>> K_List;
    DBforAnalysis dBforAnalysis;
    MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        dBforAnalysis = new DBforAnalysis(this, "POS1.db", null,1);
        chart = (BarChart) findViewById(R.id.chart1);

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "Sales");
        Bardataset.setColors(color);

        Bardataset.setBarSpacePercent(30f);

        BARDATA = new BarData(BarEntryLabels, Bardataset);
        chart.setData(BARDATA);

        chart.animateY(3000);
        //xAxis.setTextSize(23.0f);
        xAxis = chart.getXAxis();
        xAxis.setTextSize(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelRotationAngle();
        this.gestureDetector = new GestureDetector(this,this);
    }

    public void AddValuesToBARENTRY() {
        a_list = new ArrayList<>(); b_list = new ArrayList<>(); c_list = new ArrayList<>();
        d_list = new ArrayList<>(); e_list = new ArrayList<>(); f_list = new ArrayList<>();
        g_list = new ArrayList<>(); h_list = new ArrayList<>(); i_list = new ArrayList<>();
        j_list = new ArrayList<>(); k_list = new ArrayList<>(); l_list = new ArrayList<>();

        //dBforAnalysis = new MainActivity().dBforAnalysis;
        Date now = new Date();
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd");
        String s = CurrentTime.format(now);
        ArrayList<Order> orderlist = new ArrayList<>();
        orderlist = dBforAnalysis.getAllOrderS();


        for(int p = 0; p < orderlist.size(); p++){
            String amount = orderlist.get(p).getOrder_amount();
            String Date = orderlist.get(p).getOrder_date();
            String Time = orderlist.get(p).getOrder_time();
            String Price = orderlist.get(p).getOrder_Price_perMenu();
            String menu = orderlist.get(p).getOrder_FK_menuId();
            if(Date.equals(s)){
                Time = Time.substring(0,2);

                switch (Time) {
                    case "00":
                    case "01":
                        a_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "02":
                    case "03":
                        b_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "04":
                    case "05":
                        c_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "06":
                    case "07":
                        d_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "08":
                    case "09":
                        e_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "10":
                    case "11":
                        f_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "12":
                    case "13":
                        g_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "14":
                    case "15":
                        h_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "16":
                    case "17":
                        i_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "18":
                    case "19":
                        j_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "20":
                    case "21":
                        k_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                    case "22":
                    case "23":
                        l_list.add(Integer.parseInt(amount) * Integer.parseInt(Price));
                        break;
                }
            }
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        K_List= new ArrayList<>();
        K_List.add(a_list);  K_List.add(b_list); K_List.add(c_list); K_List.add(d_list); K_List.add(e_list); K_List.add(f_list);
        K_List.add(g_list);  K_List.add(h_list); K_List.add(i_list); K_List.add(j_list); K_List.add(k_list); K_List.add(l_list);

        for (int j= 0 ; j < K_List.size(); j++){
            ArrayList<Integer> A = K_List.get(j);
            Integer sum = 0;
            try {
                for (int l = 0; l < A.size(); l++) {
                    sum = sum + A.get(l);
                }
            } catch (NullPointerException e){
            }
            arrayList.add(j, sum);
        }
        a = arrayList.get(0);
        b = arrayList.get(1);
        c = arrayList.get(2);
        d = arrayList.get(3);
        e = arrayList.get(4);
        f = arrayList.get(5);
        g = arrayList.get(6);
        h = arrayList.get(7);
        i = arrayList.get(8);
        j = arrayList.get(9);
        k = arrayList.get(10);
        l = arrayList.get(11);

        BARENTRY.add(new BarEntry(a, 0));
        BARENTRY.add(new BarEntry(b, 1));
        BARENTRY.add(new BarEntry(c, 2));
        BARENTRY.add(new BarEntry(d, 3));
        BARENTRY.add(new BarEntry(e, 4));
        BARENTRY.add(new BarEntry(f, 5));
        BARENTRY.add(new BarEntry(g, 6));
        BARENTRY.add(new BarEntry(h, 7));
        BARENTRY.add(new BarEntry(i, 8));
        BARENTRY.add(new BarEntry(j, 9));
        BARENTRY.add(new BarEntry(k, 10));
        BARENTRY.add(new BarEntry(l, 11));
    }

    public void AddValuesToBarEntryLabels() {
        BarEntryLabels.add("0-2");
        BarEntryLabels.add("2-4");
        BarEntryLabels.add("4-6");
        BarEntryLabels.add("6-8");
        BarEntryLabels.add("8-10");
        BarEntryLabels.add("10-12");
        BarEntryLabels.add("12-14");
        BarEntryLabels.add("14-16");
        BarEntryLabels.add("16-18");
        BarEntryLabels.add("18-20");
        BarEntryLabels.add("20-22");
        BarEntryLabels.add("22-24");
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent motionEvent) { }
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }
    @Override
    public void onLongPress(MotionEvent motionEvent) { }
    @Override

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float diffY = motionEvent1.getY() - motionEvent.getY();
        if (diffY < 0) {
            // Create the Snackbar
            LayoutInflater mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = findViewById(R.id.bar_statistics_layout);
            ConstraintLayout.LayoutParams objLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
            // Get the Snackbar's layout view
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setPadding(0,0,0,0);


            // Inflate our custom view
            View snackView = getLayoutInflater().inflate(R.layout.activity_snackbar_statistics2, null);
            // Configure the view
            AmountStastisticBtn = (Button) snackView.findViewById(R.id.AmountStastisticBtn);

            AmountStastisticBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvStaIntent = new Intent(getApplication(), PieChartDataActivity.class);
                    startActivity(mvStaIntent);
                }
            });

            SalesStatisticBtn = (Button) snackView.findViewById(R.id.SalesStatisticBtn);
            SalesStatisticBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvStaIntent = new Intent(getApplication(), BarChartActivity.class);
                    startActivity(mvStaIntent);
                }
            });

            // Add the view to the Snackbar's layout
            layout.addView(snackView, objLayoutParams);
            // Show the Snackbar
            snackbar.show();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

}

