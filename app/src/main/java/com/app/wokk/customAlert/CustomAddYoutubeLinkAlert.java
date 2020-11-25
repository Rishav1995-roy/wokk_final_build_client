package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.app.wokk.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static android.graphics.Color.TRANSPARENT;

public class CustomAddYoutubeLinkAlert extends Dialog {

    public Button btnOk;
    public Button btncancel;
    public TextInputEditText etTitle;
    public TextInputEditText etCaption;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video_details);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable) (new ColorDrawable(TRANSPARENT)));
        btnOk=findViewById(R.id.btnOk);
        btncancel=findViewById(R.id.btncancel);
        etTitle=findViewById(R.id.etTitle);
        etCaption=findViewById(R.id.etCaption);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public CustomAddYoutubeLinkAlert(@NonNull Context context) {
        super(context);
    }
}
