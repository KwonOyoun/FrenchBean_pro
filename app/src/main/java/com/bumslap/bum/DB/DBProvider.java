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
    private SQLiteDatabase db ;
    private DBHelper dbHelper;


    public DBProvider(Context ctx){
        context = ctx;
    }


    public DBProvider open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public Cursor getData(String sql){
        return db.rawQuery(sql, null);
    }


    public void queryData(String sql){
        db  = dbHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public void insertData(String name, String price, String cost, byte[] image){
        db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO MENU_TABLE VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, cost);
        statement.bindBlob(4, image);

        statement.executeInsert();
    }
    public void updateData(String name, String price, String cost, byte[] image, String id){
        db = dbHelper.getWritableDatabase();
        String sql = "UPDATE MENU_TABLE SET NAME = ?, PRICE = ?, COST = ?, IMAGE = ? WHERE ID = ?;";

        SQLiteStatement statement = db.compileStatement(sql);

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
        stringBuffer.append("DELETE FROM MENU_TABLE WHERE ID = ? ");
        db.execSQL(stringBuffer.toString(), new Object[]{id});
        db.delete("MENU_TABLE", id   + " = ? ", new String[] { id });
    }

    public void close(){
        dbHelper.close();
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



