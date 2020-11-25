package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.app.wokk.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static android.graphics.Color.TRANSPARENT;

public class CustomPictureDetailsAlert extends Dialog {

    public Button btnOk;
    public Button btncancel;
    public TextInputEditText etTitle;
    public TextInputEditText etCaption;
    public ImageView ivPreview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picture_details);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable) (new ColorDrawable(TRANSPARENT)));
        btnOk=findViewById(R.id.btnOk);
        btncancel=findViewById(R.id.btncancel);
        etTitle=findViewById(R.id.etTitle);
        etCaption=findViewById(R.id.etCaption);
        ivPreview=findViewById(R.id.ivPreview);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
    public CustomPictureDetailsAlert(@NonNull Context context) {
        super(context);
    }
}
