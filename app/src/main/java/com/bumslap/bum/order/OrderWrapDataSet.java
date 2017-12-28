package com.bumslap.bum.order;

import java.util.ArrayList;

/**
 * Created by oyoun on 17. 12. 19.
 */

public class OrderWrapDataSet {
    private String BillTitleNumber;
    private ArrayList BillAllData;
    private String Auto_increasment;

    public OrderWrapDataSet(){

    }

    public String getAuto_increasment(){
        return Auto_increasment;
    }

    public void setAuto_increasment(String auto_increasment){
        this.Auto_increasment = auto_increasment;
    }

    public ArrayList getBillAllData(){
        return BillAllData;
    }

    public void setBillAllData(ArrayList billAllData){
        this.BillAllData = billAllData;
    }

    public String getBillTitleNumber(){
        return BillTitleNumber;
    }

    public void setBillTitleNumber(String billTitleNumber){
        this.BillTitleNumber = billTitleNumber;
    }
}
