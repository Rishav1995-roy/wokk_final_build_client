package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.wokk.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static android.graphics.Color.TRANSPARENT;

public class EditCardDialog extends Dialog {

    public Button btnOk, btncancel;
    public TextView tvHeading;
    public TextInputEditText etTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_card_dialog);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable) (new ColorDrawable(TRANSPARENT)));
        btnOk=findViewById(R.id.btnOk);
        btncancel=findViewById(R.id.btncancel);
        etTitle=findViewById(R.id.etTitle);
        tvHeading=findViewById(R.id.tvHeading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public EditCardDialog(@NonNull Context context) {
        super(context);
    }
}
