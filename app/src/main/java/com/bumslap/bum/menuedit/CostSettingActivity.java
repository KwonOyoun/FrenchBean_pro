package com.bumslap.bum.menuedit;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumslap.bum.DB.Cost;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.Menu;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.POSproject.SignInActivity;
import com.bumslap.bum.R;
import com.bumslap.bum.order.OrderActivity;
import com.bumslap.bum.settings.UserSettingActivity;
import com.bumslap.bum.statistics.BarChartActivity;
import com.bumslap.bum.statistics.SalesStatus2Activity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CostSettingActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,NavigationView.OnNavigationItemSelectedListener {
    Button MenuSetBtn, CostSetBtn;
    Intent mvSetIntent;
    private GestureDetector gestureDetector;
    FloatingActionButton floatingActionButton_cost;
    DBforAnalysis dBforAnalysis;
    Spinner spinnerMenu;
    RecyclerView recyclerView, recyclerView2;
    CostAdapter costAdapter;
    ArrayList<Cost> arrayList, costAllData;
    SQLiteDatabase mdb;
    Typeface mTypeface;
    static Context context;
    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;
    CostUpdateAdapter costUpdateAdapter;
    View layout;
    ArrayList<String> MenuName, MenuPrice, MenuId;
    ArrayList<Menu> MenuallData;
    TextView menuPrice;
    String price, name, menu = "";
    TextView sumCost, margin;
    String menu_name, menu_id;
    Integer position;
    String menuprice;
    int CostTotal = 0;

    Intent intent;

    private DBProvider dbProvider;

    ImageButton exitBtn;

    FloatingActionButton fab1, fab2, fab4;

    Animation fabOpen, fabClose, rotateForward, rotateBackward, costanim;
    boolean isOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_navi);

        //snackbar
        this.gestureDetector = new GestureDetector(this,this);
        arrayList = new ArrayList<Cost>();  //이름수정
        costAllData = new ArrayList<Cost>();
        dbProvider = new DBProvider(this);
        dbProvider.open();

        dBforAnalysis = new DBforAnalysis(this);

        //mdb = dBforAnalysis.getWritableDatabase();
        //costAdapter = new CostAdapter(arrayList, this);
        costAdapter = new CostAdapter(costAllData, this);
        context = this;

        menuPrice = (TextView)findViewById(R.id.textView3);
        sumCost = (TextView)findViewById(R.id.Sumcost);
        margin =(TextView)findViewById(R.id.textView6);
        //spinner
        MenuallData = dBforAnalysis.getMenuAllData();
        MenuName = new ArrayList<>();
        MenuPrice = new ArrayList<>();
        MenuId = new ArrayList<>();
        for(int i = 0 ; i <MenuallData.size(); i++) {
            MenuName.add(MenuallData.get(i).getMenu_name().toString());
            MenuPrice.add(MenuallData.get(i).getMenu_price());
            MenuId.add(MenuallData.get(i).getMenu_id());
            //Menu = dBforAnalysis.getMenuname();
        }

        spinnerMenu = (Spinner)findViewById(R.id.spinnerMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                MenuName);
        spinnerMenu.setAdapter(adapter);
        spinnerMenu.setSelection(0);


        spinnerMenu.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               kk();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kk();


        Toolbar toolbar = findViewById(R.id.toolbar_cost);
        setSupportActionBar(toolbar);//0xFFB9C18F
        getSupportActionBar().setTitle("목록 세팅");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onResume() {
        super.onResume();
        //modal dialog
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;

        // 상태바와 메뉴바의 크기를 포함*/
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) { }

        //RecyclerView
        //Cost cost = new Cost();
        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(costAdapter);



    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) { return false; }

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
            View view = findViewById(R.id.cost_setting_layout);
            ConstraintLayout.LayoutParams objLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(Color.WHITE);
            // Get the Snackbar's layout view
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setPadding(0,0,0,0);

            // Inflate our custom view
            View snackView = getLayoutInflater().inflate(R.layout.activity_snackbar_setting, null);
            // Configure the view
            MenuSetBtn = (Button)snackView.findViewById(R.id.MenuSetBtn);
            CostSetBtn = (Button)snackView.findViewById(R.id.CostSetBtn);

            MenuSetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvSetIntent = new Intent(getApplication(), MenuSettingActivity.class);
                    startActivity(mvSetIntent);
                }
            });

            CostSetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvSetIntent = new Intent(getApplication(), CostSettingActivity.class);
                    startActivity(mvSetIntent);
                }
            });

            // Add the view to the Snackbar's layout
            layout.addView(snackView, objLayoutParams);
            // Show the Snackbar
            snackbar.show();
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        else if(id == R.id.nav_share){

            FirebaseAuth.getInstance().signOut();
            intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.cost_set_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.edit){
            initiatePopupWindow();

        }




        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    private void initiatePopupWindow() {
        try {
            //modal 창
            final Cost cost = new Cost();
            LayoutInflater inflater = (LayoutInflater) CostSettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.activity_cost_update, (ViewGroup)findViewById(R.id.view));
            pwindo = new PopupWindow(layout, mWidthPixels, mHeightPixels, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //recyclerview
            arrayList = dBforAnalysis.getAllCostData();
            recyclerView2 = (RecyclerView)layout.findViewById(R.id.rv);

            costUpdateAdapter = new CostUpdateAdapter(costAllData, this);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView2.setAdapter(costUpdateAdapter);

            fab1 = (FloatingActionButton)layout.findViewById(R.id.fab1);
            fab2 = (FloatingActionButton) layout.findViewById(R.id.fab2);
            fab4 = (FloatingActionButton) layout.findViewById(R.id.fab4);
            exitBtn = (ImageButton) layout.findViewById(R.id.exitBtn);

            exitBtn.setOnClickListener(closeclick);


            fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
            fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

            rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
            rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);


            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateFab();
                }
            });
            fab2.setOnClickListener(new View.OnClickListener(){
                Cost firIngradient;
                @Override
                public void onClick(View view) {
                    animateFab();
                    //DB add
                    firIngradient = new Cost();
                    try {
                        menu = spinnerMenu.getSelectedItem().toString();
                    }
                    catch (NullPointerException e){
                        menu = "";
                    }
                    firIngradient.setCost_name("");
                    firIngradient.setCost_price("");

                    Menu_id();
                    firIngradient.setCost_FK_menuId(Integer.parseInt(menu_id));
                    dBforAnalysis.addCost(firIngradient);

                    costAllData = dBforAnalysis.getMenuMatchCostData(menu_id);
                    costUpdateAdapter.changdAddItem(costAllData);
                }
            });

            fab4.setOnClickListener(new View.OnClickListener() {
                int flag = 0;
                View v;
                ArrayList<Integer> pos = new ArrayList<>();
                @Override
                public void onClick(View view) {
                    //DB delete

                    recyclerView2 = (RecyclerView)layout.findViewById(R.id.rv);
                    String id = Menu_id();
                    costAllData = dBforAnalysis.getMenuMatchCostData(menu_id);
                    CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
                    int lengthOfcheck = recyclerView2.getChildCount();
                    if(flag == 0) {

                        for (int i = 0; i < lengthOfcheck; i++) {
                            v = recyclerView2.getChildAt(i);
                            checkBox = v.findViewById(R.id.checkBox);
                            checkBox.setChecked(false);
                            checkBox.setVisibility(View.VISIBLE);
                            if(i == lengthOfcheck-1){
                                flag++;
                            }
                        }
                    }
                    else {
                        for (int i = lengthOfcheck-1; i>= 0 ; i--) {
                            v = recyclerView2.getChildAt(i);
                            checkBox = v.findViewById(R.id.checkBox);
                            boolean checked = checkBox.isChecked();
                            if (checked == true) {
                                id = costAllData.get(i).getCost_id().toString();
                                dBforAnalysis.deleteCost(Integer.parseInt(id));
                                costAllData.remove(i);
                            }
                        }
                        costUpdateAdapter.changeItem(costAllData);
                        animateFab();
                        flag = 0;

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener closeclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            kk();
            Cost firIngradient;
            Cost cost = new Cost();
            Integer totalcost = 0;
            View v;
            EditText Ingradient_name;
            EditText Ingradient_price;
            Button button;
            recyclerView2 = (RecyclerView)layout.findViewById(R.id.rv);
            int lengthOfRec = recyclerView2.getChildCount();
            for (int i=0;i< lengthOfRec; i++){
                v = recyclerView2.getChildAt(i);
                Ingradient_name = v.findViewById(R.id.editText);
                Ingradient_price = v.findViewById(R.id.editText3);
                try {
                    name = Ingradient_name.getText().toString();
                    price = Ingradient_price.getText().toString();
                    Integer p = 0;
                    if(price.equals("")){
                        p = 0;
                    }
                    else {
                        p = Integer.parseInt(price);
                    }
                    totalcost = totalcost + p;
                }
                catch (NullPointerException e){
                    name = "";
                    price = "";
                }
                firIngradient = new Cost();
                firIngradient.setCost_id(costAllData.get(i).getCost_id());
                firIngradient.setCost_name(name);
                firIngradient.setCost_price(price);
                dBforAnalysis.updateCost(firIngradient);
                dBforAnalysis.updateMenucost(totalcost, menu_id);
            }
            dBforAnalysis.deletenullcost();
            costAllData = dBforAnalysis.getMenuMatchCostData(menu_id);
            costAdapter.changeItem(costAllData);
            pwindo.dismiss();
            c();
        }
    };

    public void c(){
        menuprice = changemoney();
        Menu_id();

        costAllData = dBforAnalysis.getMenuMatchCostData(menu_id);
        int CostTotal = 0;
        for(int k=0; k<costAllData.size(); k++){
            if(isNumber(costAllData.get(k).getCost_price()))
                CostTotal = CostTotal + Integer.parseInt(costAllData.get(k).getCost_price());
        }

        sumCost.setText(String.valueOf(CostTotal)+" 원");
        int mar;
        try {
            mar = Integer.parseInt(menuprice) - CostTotal;
        }
        catch (NumberFormatException e){
            menuprice = "0";
            mar = Integer.parseInt(menuprice) - CostTotal;

        }
        margin.setText(String.valueOf(mar)+" 원");

        costAdapter.changeItem(costAllData);
    }


    public static boolean isNumber(String str){
        boolean result = false;
        try{
            Double.parseDouble(str) ;
            result = true ;
        }catch(Exception e){}
        return result ;
    }

    public String Menu_id(){
        try {
            menu_name = spinnerMenu.getSelectedItem().toString();
            menu_id = dBforAnalysis.getMenuIdData(menu_name);
            return menu_id;
        }
        catch (NullPointerException e){
            menu_id = "0";
            return menu_id;
        }
    }

    private void animateFab(){

        if(isOpen){
            fab1.startAnimation(rotateBackward);
            fab2.startAnimation(fabOpen);
            fab4.startAnimation(fabOpen);
            fab2.setClickable(true);
            fab4.setClickable(true);
            isOpen = false;
        }
        else{
            fab1.startAnimation(rotateForward);
            fab2.startAnimation(fabClose);
            fab4.startAnimation(fabClose);
            fab2.setClickable(false);
            fab4.setClickable(false);
            isOpen = true;
        }


    }
    private String changemoney() {
        position = spinnerMenu.getSelectedItemPosition();
        try {
            menuprice = MenuallData.get(position).getMenu_price();
        }
        catch (IndexOutOfBoundsException e){
            menuprice ="";
        }

        try {
            menuPrice.setText(menuprice);
        }
        catch (IndexOutOfBoundsException e){
            menuPrice.setText("");
        }
        return menuprice;
    }


    public void kk(){
        menuprice = changemoney();
        Menu_id();

        costAllData = dBforAnalysis.getMenuMatchCostData(menu_id);
        for(int k=0; k<costAllData.size(); k++){
            if(isNumber(costAllData.get(k).getCost_price()))
                CostTotal = CostTotal + Integer.parseInt(costAllData.get(k).getCost_price());
        }

        sumCost.setText(String.valueOf(CostTotal)+" 원");


        int mar;
        try {
            mar = Integer.parseInt(menuprice) - CostTotal;
        }
        catch (NumberFormatException e){
            menuprice = "0";
            mar = Integer.parseInt(menuprice) - CostTotal;

        }
        margin.setText(String.valueOf(mar)+" 원");

        costAdapter.changeItem(costAllData);


    }


}