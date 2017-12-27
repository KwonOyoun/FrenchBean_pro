package com.bumslap.bum.menuedit;

import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.EditText;
import android.widget.Toast;

import com.bumslap.bum.R;

import java.util.regex.Pattern;

/**
 * Created by jaein on 12/14/17.
 */

public class ViewHolderCostUpdate extends RecyclerView.ViewHolder {
    EditText Ingradient_name, Ingradient_price;
    CheckBox checkBox;
    public ViewHolderCostUpdate(final View itemView) {
        super(itemView);
        Ingradient_name = (EditText) itemView.findViewById(R.id.editText);
        Ingradient_price = (EditText) itemView.findViewById(R.id.editText3);
        checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);

        Ingradient_price.setFilters(new InputFilter[]{filterNum});
    }

    protected InputFilter filterNum = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Pattern ps = Pattern.compile("^[0-9]+$");
            if(ps.matcher(charSequence).matches()){
                return charSequence;
            }
            return "";
        }
    };

}
