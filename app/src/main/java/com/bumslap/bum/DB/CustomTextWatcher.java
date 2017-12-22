package com.bumslap.bum.DB;


import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by min on 12/22/17.
 */

public class CustomTextWatcher implements TextWatcher {
    @SuppressWarnings("unused")
    private EditText mEditText;
    String strAmount = ""; // 임시 저장값 (콤마)

    public CustomTextWatcher(EditText e) {
        mEditText = e;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(strAmount)) { // StackOverflow 방지
            strAmount = makeStringComma(s.toString().replace(",", ""));
            mEditText.setText(strAmount);

            Editable e = mEditText.getText();

            Selection.setSelection(e, strAmount.length());

        }
    }

        protected String makeStringComma(String str) {    // 천단위 콤마 처리
            if (str.length() == 0)
                return "";
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat(",###");
            return format.format(value);
        }

    }



