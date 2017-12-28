package com.bumslap.bum.DB;


import java.text.SimpleDateFormat;

/**
 * Created by jaein on 12/7/17.
 */

public class Order {

    private String Order_amount;
    private String Order_date;
    private String Order_time;
    private String Order_FK_menuId;
    private String Order_number="ll";
    private String Order_Price_perMenu;
    private String Order_Table_number;

    public Order() {
    }

    public Order(String order_amount, String order_date, String order_time, String order_FK_menuId, String order_number, String order_Price_perMenu, String order_Table_number) {
        Order_amount = order_amount;
        Order_date = order_date;
        Order_time = order_time;
        Order_FK_menuId = order_FK_menuId;
        Order_number = order_number;
        Order_Price_perMenu = order_Price_perMenu;
        Order_Table_number = order_Table_number;

    }

    public String getOrder_Table_number(){ return  Order_Table_number; }

    public void setOrder_Table_number(String order_Table_number) { this.Order_Table_number = order_Table_number; }


    public String getOrder_Price_perMenu(){ return  Order_Price_perMenu; }

    public void setOrder_Price_perMenu(String order_Price_perMenu) { this.Order_Price_perMenu = order_Price_perMenu; }

    public String getOrder_amount(){
        return Order_amount;
    }

    public void setOrder_amount(String Order_amount){
        this.Order_amount = Order_amount;
    }

    public String  getOrder_date(){
        return Order_date;
    }

    public void setOrder_date(String  Order_date){
        this.Order_date = Order_date;
    }

    public String getOrder_time(){
        return Order_time;
    }

    public void setOrder_time(String Order_time){
        this.Order_time = Order_time;
    }
    public String getOrder_FK_menuId(){
        return Order_FK_menuId;
    }

    public void setOrder_FK_menuId(String Order_FK_menuId){
        this.Order_FK_menuId = Order_FK_menuId;
    }
    public String getOrder_number(){ return Order_number;}

    public void setOrder_number(String Order_number){
        this.Order_number = Order_number;
    }

}
