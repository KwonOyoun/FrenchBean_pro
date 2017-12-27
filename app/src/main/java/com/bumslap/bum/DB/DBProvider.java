package com.bumslap.bum.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DecimalFormat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by min on 12/15/17.
 */

public class DBProvider {
    private Context context;
    private SQLiteDatabase SQLdb ;
    private DBHelper dbHelper;
    private DBforAnalysis dBforAnalysis;


    public DBProvider(Context ctx){
        context = ctx;
    }


    public DBProvider open() throws SQLException {
        dBforAnalysis = new DBforAnalysis(context);
        SQLdb = dBforAnalysis.getWritableDatabase();
        return this;
    }


    public Cursor getData(String sql){
        return SQLdb.rawQuery(sql, null);
    }

//main 에서 한 번만 실행 시켜 주는 곳이다.
    public void queryData(){
        SQLdb  = dBforAnalysis.getWritableDatabase();
        //String도 가능하지만, StringBuffer 가 Query 만들기 더 편하다.
        StringBuffer sbMenu = new StringBuffer();
        sbMenu.append(" CREATE TABLE IF NOT EXISTS MENU_TABLE ( ");
        sbMenu.append(" MENU_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sbMenu.append(" MENU_NAME TEXT, ");
        sbMenu.append(" MENU_IMAGE BLOG, ");
        sbMenu.append(" MENU_PRICE TEXT,");
        sbMenu.append(" MENU_COST TEXT); ");

        // SQLite Database로 쿼리 실행
        SQLdb.execSQL(sbMenu.toString());

        StringBuffer sbOrder = new StringBuffer();
        sbOrder.append(" CREATE TABLE IF NOT EXISTS ORDER_TABLE ( ");
        sbOrder.append(" ORDER_AMOUNT TEXT, ");
        sbOrder.append(" ORDER_DATE TEXT, ");
        sbOrder.append(" ORDER_TIME TEXT, ");
        sbOrder.append(" ORDER_NUMBER TEXT, ");
        sbOrder.append(" ORDER_FK_MENUID INTEGER, ");
        sbOrder.append(" ORDER_MENU_PRICE TEXT);");

        SQLdb.execSQL(sbOrder.toString());

        StringBuffer sbCost = new StringBuffer();
        sbCost.append(" CREATE TABLE IF NOT EXISTS COST_TABLE (");
        sbCost.append(" COST_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sbCost.append(" COST_NAME TEXT, ");
        sbCost.append(" COST_PRICE TEXT,");
        sbCost.append(" COST_FK_MENUID INTEGER );");
        SQLdb.execSQL(sbCost.toString());
    }

    public void insertData(String name, String price, String cost, byte[] image){
        SQLdb = dBforAnalysis.getWritableDatabase();
        String sql = "INSERT INTO MENU_TABLE VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = SQLdb.compileStatement(sql);

        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindString(3, price);
        statement.bindString(4, cost);


        statement.executeInsert();
    }
    public void updateData(String name, String price, String cost, byte[] image, String id){
        SQLdb = dBforAnalysis.getWritableDatabase();
        String sql = "UPDATE MENU_TABLE SET MENU_NAME = ?, MENU_PRICE = ?, MENU_COST = ?, MENU_IMAGE = ? WHERE MENU_ID = ?;";

        SQLiteStatement statement = SQLdb.compileStatement(sql);

        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, cost);
        statement.bindBlob(4, image);
        statement.bindString(5, id);

        statement.executeInsert();
    }
    public void deleteData(String id) {


        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DELETE FROM MENU_TABLE WHERE MENU_ID = ? ");
        SQLdb.execSQL(stringBuffer.toString(), new Object[]{id});
        SQLdb.delete("MENU_TABLE", id   + " = ? ", new String[] { id });
    }

    public void deleteCostData(String id) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DELETE FROM COST_TABLE WHERE COST_FK_MENUID = ? ");
        SQLdb.execSQL(stringBuffer.toString(), new Object[]{id});
        //SQLdb.delete("COST_TABLE", id   + " = ? ", new String[] { id });
    }

    public void close(){
        dBforAnalysis.close();
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {
        Bitmap Bitmapimage = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return Bitmapimage ;
    }

    //editText 금액 입력시 1,000원 단위 콤마 생성 메소드 (editText 수는 2개로 설정)
    public void editTextCurrencyCommaChange(EditText editText1, EditText editText2, DecimalFormat decimalFormat,  String result ){

        //사용할 클래스에 아래 DecimalFormat 변수 선언 및 인스턴스 필요
        //String result="";




    }




}



