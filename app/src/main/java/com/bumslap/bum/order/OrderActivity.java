package com.bumslap.bum.order;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.DB.DBHelper;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.MenuListAdapter;
import com.bumslap.bum.DB.Order;
import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.R;
import com.bumslap.bum.menuedit.MenuSettingActivity;
import com.bumslap.bum.settings.UserSettingActivity;
import com.bumslap.bum.statistics.BarChartActivity;
import com.bumslap.bum.statistics.SalesStatus2Activity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class OrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView selectRecyclerView;
    int selectedItemLength;


    FloatingActionButton floatingAddBtn;
    Intent intent;
    GridView gridView;
    ArrayList<com.bumslap.bum.DB.Menu> Menulist;
    com.bumslap.bum.DB.MenuListAdapter menuListAdapter = null;

    RecyclerView billRecyclerView;
    RecyclerView.Adapter Adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RealtimeOrder> Billordermenu;
    OrderMenuSelectAdapter orderMenuSelectAdapter;
    DBProvider db;

    String str_device;
    public static DBHelper dbforAnalysis;

    ArrayList<HashMap<String, Integer>> OrderList;
    HashMap<String, Integer> Ordermap;

    HashMap<String, ArrayList<Order>> toWrapmap;

    ArrayList<Order> Order_menu_List;
    long CurrentTimeCall;
    Date CurrentDateCall;
    SimpleDateFormat CurrentDate;
    SimpleDateFormat CurrentTime;
    String CurrentTimes, CurrentDates;
    int Order_Amount;

    ArrayList<OrderWrapDataSet> orderwraplist;
    OrderWrapAdapter orderWrapAdapter;
    OrderWrapDataSet orderWrapDataSet;

    public DBforAnalysis newdbforAnalysis;
    Order putOrder;
    static Context context;
    int billnumberposition = 0;

    //Button addpositionBTN;

    HashMap<String, HashMap<String, Integer>> hashmapInhashmap;

    Button OrderPayBTN, OrderCancelBTN;


    RecyclerView SelectRecyclerView;
    int SelectLength;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우
        setContentView(R.layout.activity_order);
        // setContentView()가 호출되기 전에 setRequestedOrientation()이 호출되어야 함
        //setTitle("오늘도 달려 보세");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingAddBtn = findViewById(R.id.floatingAddBtn);
        context = this;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


        gridView = (GridView) findViewById(R.id.gridview);

        Menulist = new ArrayList<>();

        menuListAdapter = new MenuListAdapter(this, R.layout.order_menu_item, Menulist);
        gridView.setAdapter(menuListAdapter);
        db = new DBProvider(this);
        db.open();
        dbforAnalysis = new DBHelper(this);

        newdbforAnalysis = new DBforAnalysis(this);
        try {
            Cursor cursor = db.getData("SELECT * FROM MENU_TABLE");
            Menulist.clear();
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(2);
                String price = cursor.getString(3);
                String cost = cursor.getString(4);


                Menulist.add(new com.bumslap.bum.DB.Menu(id, name, image, price, cost));
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        menuListAdapter.notifyDataSetChanged();

        Billordermenu = new ArrayList<>();

        // addpositionBTN = (Button)findViewById(R.id.addpositionBTN);

        floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                billnumberposition = orderwraplist.size() - 1;
                billnumberposition++;
            }
        });


        billRecyclerView = (RecyclerView) findViewById(R.id.order_recycler);


        OrderList = new ArrayList<HashMap<String, Integer>>();


        Order_menu_List = new ArrayList<Order>();


        hashmapInhashmap = new HashMap<String, HashMap<String, Integer>>();
        Ordermap = new HashMap<String, Integer>();


        toWrapmap = new HashMap<String, ArrayList<Order>>();

        orderWrapDataSet = new OrderWrapDataSet();
        OrderWrapDataSet orderWrapDataSet1 = new OrderWrapDataSet();
        orderWrapDataSet1.setBillTitleNumber("asdas");
        orderwraplist = new ArrayList<OrderWrapDataSet>();


        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false); //, LinearLayoutManager.HORIZONTAL, false
        orderWrapAdapter = new OrderWrapAdapter(orderwraplist, getApplicationContext());
        billRecyclerView.setLayoutManager(layoutManager);
        billRecyclerView.setAdapter(orderWrapAdapter);


        billRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {

                    View reV = rv.findChildViewUnder(e.getX(), e.getY());
                    billnumberposition = rv.getChildAdapterPosition(reV);

                    billRecyclerView.scrollToPosition(billnumberposition);
                    Toast.makeText(getApplicationContext(), String.valueOf(billnumberposition), Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Order_menu_List.clear();
                //orderwraplist.clear();
                //orderwraplist = new ArrayList<OrderWrapDataSet>();
                Order_menu_List = new ArrayList<Order>();
                orderWrapDataSet = new OrderWrapDataSet();
                String MenuID = Menulist.get(position).getMenu_id();

                String Price = Menulist.get(position).getMenu_price();
                int Amount = 0;

                //billRecyclerView.setLayoutManager(layoutManager);
                //billRecyclerView.setItemAnimator(new DefaultItemAnimator());
                String bp = String.valueOf(billnumberposition);
                // billRecyclerView.setAdapter(Adapter);
                //billRecyclerView.smoothScrollBy(200, 100);
                Toast.makeText(getApplicationContext(), "" + position + "  " + MenuID + " " + Price, Toast.LENGTH_LONG).show();


                //if(hashmapInhashmap.size() == (Integer.parseInt(bp)+1) || hashmapInhashmap.size() == 0) {
                if (hashmapInhashmap.get(bp) == null) {
                    Ordermap = new HashMap<String, Integer>();
                    hashmapInhashmap.put(bp, Ordermap);
                    if (hashmapInhashmap.get(bp).get(MenuID) == null) {
                        Ordermap.put(MenuID, 0);
                        hashmapInhashmap.put(bp, Ordermap);
                    }


                }
                Ordermap = hashmapInhashmap.get(bp);
                if (Ordermap.get(MenuID) == null) {
                    Ordermap.put(MenuID, 0);
                }
                hashmapInhashmap.put(bp, Ordermap);
                //hashmapInhashmap.put(bp,Ordermap);
                //}

                //Amount = Ordermap.get(MenuID);

                //null로 들어가는 것을 고려하여야 한다.
                try {

                    for (int j = 0; j <= toWrapmap.get(bp).size(); j++) {
                        String menuId = toWrapmap.get(bp).get(j).getOrder_FK_menuId();
                        int intmenuid = Integer.parseInt(menuId);
                        if (intmenuid == Integer.parseInt(MenuID)) {
                            Amount = Integer.parseInt(toWrapmap.get(bp).get(j).getOrder_amount());

                            Ordermap = new HashMap<String, Integer>();
                            Ordermap.put(MenuID, ++Amount);
                            hashmapInhashmap.put(bp, Ordermap);


                        }
                    }

                } catch (Exception ex) {
                    ex.getCause();
                }


                OrderList.add(Ordermap);
                CurrentTimeCall = System.currentTimeMillis();
                CurrentDateCall = new Date(CurrentTimeCall);
                CurrentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                CurrentTime = new SimpleDateFormat("hh-mm-ss", Locale.KOREA);
                CurrentTimes = CurrentDate.format(CurrentDateCall);
                Order_Amount = hashmapInhashmap.get(bp).get(MenuID);
                if (Order_Amount == 0) {
                    Order_Amount = 1;
                }


                if (toWrapmap.get(bp) != null) {
                    Order_menu_List = toWrapmap.get(bp);
                }
                Order_menu_List.add(new Order(String.valueOf(Order_Amount), CurrentDate.toString(), CurrentTime.toString(), MenuID, String.valueOf(billnumberposition), "0"));

                try {
                    int k = Order_menu_List.size();
                    for (int i = 0; i < k; i++) {
                        for (int j = 0; j <= i; j++) {
                            if (Integer.parseInt(Order_menu_List.get(i).getOrder_FK_menuId()) == Integer.parseInt(Order_menu_List.get(j).getOrder_FK_menuId())) {
                                Order_menu_List.set(j, Order_menu_List.get(i));

                                k++;
                                if (k == Order_menu_List.size() * 2 + 1) {
                                    Order_menu_List.remove(Order_menu_List.size() - 1);
                                }
                            }
                        }
                    }
                } catch (Exception ec) {
                    ec.getCause();

                }


                toWrapmap.put(bp, Order_menu_List);
                //Order_menu_List_toWrap.add(toWrapmap);
                orderWrapDataSet.setBillAllData(Order_menu_List);
                orderWrapDataSet.setBillTitleNumber(bp);
                orderwraplist.add(orderWrapDataSet);

                try {
                    int k = orderwraplist.size();
                    for (int i = 0; i < k; i++) {
                        for (int j = 0; j <= i; j++) {
                            if (Integer.parseInt(orderwraplist.get(i).getBillTitleNumber()) == Integer.parseInt(orderwraplist.get(j).getBillTitleNumber())) {
                                orderwraplist.set(j, orderwraplist.get(i));

                                k++;
                                if (k == orderwraplist.size() * 2 + 1) {
                                    orderwraplist.remove(orderwraplist.size() - 1);
                                }
                            }
                        }
                    }
                } catch (Exception ec) {

                    ec.getCause();

                }


                layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false); //, LinearLayoutManager.HORIZONTAL, false
                orderWrapAdapter = new OrderWrapAdapter(orderwraplist, getApplicationContext());
                billRecyclerView.setLayoutManager(layoutManager);
                billRecyclerView.setAdapter(orderWrapAdapter);
                billRecyclerView.scrollToPosition(billnumberposition);


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View orderlayout = inflater.inflate(R.layout.order_bills_layout, (ViewGroup) R.id.billcon);
            // selectRecyclerView = (RecyclerView) orderlayout.findViewById(R.id.Bill_order_list);
            // selectedItemLength = selectRecyclerView.getChildCount();

            String ItemName = intent.getStringExtra("item");
            String qty = intent.getStringExtra("quantity");
            Toast.makeText(OrderActivity.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();
            // Get extra data included in the Intent
           /* String ItemName = intent.getStringExtra("item");
            String qty = intent.getStringExtra("quantity");
            Toast.makeText(OrderActivity.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();*/
            }



    };


    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_start) {
            intent = new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prepare) {
            intent = new Intent(getApplicationContext(), MenuSettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_analysis) {
            intent = new Intent(getApplicationContext(), BarChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_usersetting) {
            intent = new Intent(getApplicationContext(), UserSettingActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_finish){
            intent = new Intent(getApplicationContext(), SalesStatus2Activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
