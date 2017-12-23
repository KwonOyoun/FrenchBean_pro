package com.bumslap.bum.statistics;


import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Menu;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PieChartDataActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    static final String[] LIST_MENU = {"Steak x 8", "Juice x 1", "Cola x 2"};
    Button AmountStaBtn, SalesStaBtn;
    ArrayList<String> list;
    PieChart mChart;
    private int[] yValues = {8,1,1};
    private String[] xValues = {"Steak","Juice","Cola"};
    ArrayList<Integer> y;
    ArrayList<String> x;
    ArrayList<Menu> menulist;
    DBProvider db;
    private GestureDetector gestureDetector;
    Intent mvStaIntent;
    Button AmountStastisticBtn, SalesStatisticBtn;
    String name, id, s;
    MainActivity mainActivity;
    DBforAnalysis dBforAnalysis;
    ArrayList<Entry> yVals1;
    // colors for different sections in pieChart
    public static final int[] MY_COLORS = {
            Color.rgb(240,133,44),Color.rgb(27,204,133),Color.rgb(245,231,190)};

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_salesstatus_goh);

// creating data values

        mChart = (PieChart) findViewById(R.id.piechart);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setHoleRadius(50);

        mChart.setRotationEnabled(false);
        // mChart.setCenterText("50%");
        // mChart.setCenterTextSize(20);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(PieChartDataActivity.this,
                        xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        piecharddb();
        // setting sample Data for Pie Chart
        setDataForPieChart();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1 ,list) ;

        ListView listview = (ListView) findViewById(R.id.graph_listview) ;
        listview.setAdapter(adapter);

        this.gestureDetector = new GestureDetector(this,this);
    }

    public void piecharddb(){
        list = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd");
        s = CurrentTime.format(now);
        y = new ArrayList<>();
        x = new ArrayList<>();
        dBforAnalysis = new DBforAnalysis(this, "POS1.db", null,1);
        db = new DBProvider(this);
        db.open();

        menulist = new ArrayList<>();

        Cursor cursor = db.getData("SELECT * FROM MENU_TABLE");
        menulist.clear();
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
            name = cursor.getString(1);
            String price = cursor.getString(2);
            String cost = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            menulist.add(new com.bumslap.bum.DB.Menu(id, name, image, price, cost));
            x.add(id);
        }
        int yy = 0;
        yVals1 = new ArrayList<Entry>();
        ArrayList<Order> order = new ArrayList<Order>();
        order = dBforAnalysis.getAllOrderS();
        for (int i = 0; i < menulist.size(); i++) {
            for(int p = 0; p < order.size(); p++) {
                String amount = order.get(p).getOrder_amount();
                String Date = order.get(p).getOrder_date();
                String menu_id = order.get(p).getOrder_FK_menuId();

                if(Date.equals(s)) {
                    if (x.get(i).equals(menu_id)) {
                        //y.add(Integer.parseInt(amount));
                        yy = yy + Integer.parseInt(amount);
                    }
                }
            }
            //for (int j = 0; j < y.size(); j++)
            list.add(x.get(i) + " X " + yy);
            yVals1.add(new Entry(yy, i));
            yy = 0;
        }
    }


    public void setDataForPieChart() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < x.size(); i++)
            xVals.add(x.get(i));

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        // mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateY( 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);


        /*
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
       // mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateY( 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);*/
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float diffY = motionEvent1.getY() - motionEvent.getY();
        if (diffY < 0) {
            // Create the Snackbar
            LayoutInflater mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = findViewById(R.id.pie_statistics_layout);
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

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }

    }
}