package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.wokk.R;

import java.util.Objects;

import static android.graphics.Color.*;

public class CustomAlertWithOneButton extends Dialog {

    public TextView tvDesc;
    public Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_with_one_button);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable)(new ColorDrawable(TRANSPARENT)));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tvDesc=findViewById(R.id.tvDesc);
        btnOk=findViewById(R.id.btnOk);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public CustomAlertWithOneButton(@NonNull Context context) {
        super(context);
    }
}
