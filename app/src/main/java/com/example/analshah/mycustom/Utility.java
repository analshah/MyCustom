package com.example.analshah.mycustom;

import android.widget.EditText;

/**
 * Created by analll on 17-03-2017.
 */

class Utility {

    public static boolean isBlankField(EditText etPersonData)
    {
        return etPersonData.getText().toString().trim().equals("");
    }
}


