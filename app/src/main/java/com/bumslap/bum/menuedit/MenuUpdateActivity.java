package com.bumslap.bum.menuedit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.DB.CustomTextWatcher;
import com.bumslap.bum.DB.DBHelper;
import com.bumslap.bum.DB.DBProvider;
import com.bumslap.bum.DB.DBforAnalysis;
import com.bumslap.bum.DB.Menu;
import com.bumslap.bum.R;
import com.bumslap.bum.order.OrderActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.kakao.usermgmt.StringSet.id;

public class MenuUpdateActivity extends AppCompatActivity {
    Button UpdateBTN;
    EditText UpdateMenuName, UpdateMenuPrice;
    TextView UpdateMenuCost, margin;
    ImageView UpdateMenuImage;
    FloatingActionButton UpdateMenuImageBTN;
    int IMAGE_CAPTURE = 1;
    Context context = this;
    ArrayList<com.bumslap.bum.DB.Menu> menulist;
    public Bitmap Bitmapimage;

    public static DBHelper dbforAnalysis;
    public static DBProvider db;
    public DBforAnalysis newdbforanalysis;

    Menu menu;
    String stringId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_update);
        setTitle("메뉴 등록");
        init();

        //newdbforanalysis = new DBforAnalysis(this, "POS2.db", null,1);
        // DBProvider에 DBHelper 인스턴스
        db = new DBProvider(this);
        db.open();
        menulist = new ArrayList<>();

        //나중에 Main 으로 보낼 부분.(Table 생성 하는 부분.)
        //db.queryData();

        UpdateMenuImageBTN.setOnClickListener(changeimage);
        UpdateBTN.setOnClickListener(UpdateMenu);

        //UpdateMenuPrice.addTextChangedListener(new CustomTextWatcher(UpdateMenuPrice));
        //UpdateMenuCost.addTextChangedListener(new CustomTextWatcher(UpdateMenuCost));

        try{

        Bundle bundle = getIntent().getExtras();

        stringId = bundle.getString("id", "NO DATA");

        if(stringId == "NO DATA"){
            menulist.clear();
            Toast.makeText(context, "인텐트 석세스" +bundle.getString("id","NO DATA"),Toast.LENGTH_LONG).show();        }
        else{
            retrieve(stringId);
            Toast.makeText(context, "인텐트 석세스" + bundle.getString("id","DATA"),Toast.LENGTH_LONG).show();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer mar =0;
        String s = UpdateMenuCost.getText().toString();
        if(UpdateMenuCost.getText().toString().equals("")){
            //mar = Integer.parseInt(UpdateMenuPrice.getText().toString()) - 0;
            UpdateMenuCost.setText("0");
        }else {
            mar = Integer.parseInt(UpdateMenuPrice.getText().toString()) - Integer.parseInt(UpdateMenuCost.getText().toString());
        }
        margin.setText(String.valueOf(mar));

        UpdateMenuPrice.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String price = UpdateMenuPrice.getText().toString();
                String cost = UpdateMenuCost.getText().toString();
                Integer mar = 0;
                if(cost.equals("")) {
                    mar = Integer.parseInt(price) - 0;
                }
                else if(price.equals("")){
                    mar = 0- Integer.parseInt(cost);
                }
                else {
                    mar = Integer.parseInt(price) - Integer.parseInt(cost);
                }
                margin.setText(String.valueOf(mar));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        UpdateBTN = (Button) findViewById(R.id.UpdateBtn);

        UpdateMenuImage = (ImageView) findViewById(R.id.UpdateMenuImage);
        UpdateMenuName = (EditText) findViewById(R.id.UpdateMenuName);
        UpdateMenuPrice = (EditText) findViewById(R.id.UpdateMenuPrice);
        UpdateMenuCost = (TextView) findViewById(R.id.UpdateMenuCost);
        margin = (TextView)findViewById(R.id.margin2);

        UpdateMenuImageBTN = (FloatingActionButton) findViewById(R.id.UpdateMenuImageBTN);
    }

    private void retrieve(String stringId)
    {
        //Intent intent = getIntent();
        //String idid = intent.getExtras().getString("id");
        //integerId = Integer.parseInt(id);


        Cursor cursor =  db.getData("SELECT * FROM MENU_TABLE WHERE MENU_ID = " + stringId + ";");
        menulist.clear();
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            String price = cursor.getString(3);
            String cost = cursor.getString(4);

            menulist.add(new com.bumslap.bum.DB.Menu(id, name, image, price, cost));

            //byte[] to bitmap in DBProvider.class
            Bitmapimage = db.byteArrayToBitmap(image);

            UpdateMenuImage.setImageBitmap(Bitmapimage);
            UpdateMenuName.setText(name);
            UpdateMenuPrice.setText(price);
            UpdateMenuCost.setText(cost);
        }

    }


    Button.OnClickListener UpdateMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(stringId){
                case "NO DATA":
                    try {
                        db.insertData(
                                UpdateMenuName.getText().toString().trim(),
                                UpdateMenuPrice.getText().toString().trim(),
                                UpdateMenuCost.getText().toString().trim(),
                                imgaeViewToByte(UpdateMenuImage)
                        );

                        Toast.makeText(getApplicationContext(), "메뉴 등록 완료", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MenuSettingActivity.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    try {
                        db.updateData(
                                UpdateMenuName.getText().toString().trim(),
                                UpdateMenuPrice.getText().toString().trim(),
                                UpdateMenuCost.getText().toString().trim(),
                                imgaeViewToByte(UpdateMenuImage),
                                stringId
                        );

                        Toast.makeText(getApplicationContext(), "메뉴 등록 완료", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MenuSettingActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }


        }


    };

    Button.OnClickListener changeimage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final CharSequence[] items = {"Camera", "Gallery"};
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    context);

            // 제목셋팅
            alertDialogBuilder.setTitle("실행 시킬 앱");
            alertDialogBuilder.setItems(

                    items,new DialogInterface.OnClickListener()

                    {
                        public void onClick(DialogInterface dialog,
                                            int id) {

                            // 프로그램을 종료한다
                            Toast.makeText(getApplicationContext(), items[id] + " 요!? ", Toast.LENGTH_SHORT).show();
                            switch (id) {
                                case 0:
                                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, IMAGE_CAPTURE);
                                    }
                                    break;
                                case 1:
                                    int LOAD_IMAGE = 101;
                                    Intent intent = new Intent();
                                    intent.setType("image/*");   //가져오려하는 종류
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, LOAD_IMAGE);
                                    break;

                            }
                            dialog.dismiss();
                        }
                    }

            );

            // 다이얼로그 생성
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // 다이얼로그 보여주기
            alertDialog.show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UpdateMenuImage = (ImageView) findViewById(R.id.UpdateMenuImage);
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                UpdateMenuImage.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            }

        } else if (data != null) {
            Uri selectedImage = data.getData();
            InputStream inputStream = null;

            try {
                inputStream = this.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            UpdateMenuImage.setImageBitmap(bitmap);
        }
    }




    private byte[] imgaeViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }
    protected InputFilter filterNum = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Pattern ps = Pattern.compile("^[0-9]+$");
            if(ps.matcher(charSequence).matches()){
                return "";
            }
            return null;
        }
    };

}

