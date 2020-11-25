package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.app.wokk.R;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class RotateDialog extends Dialog {

    public ImageView ivRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate_loadder);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable)(new ColorDrawable(Color.TRANSPARENT)));
        ivRotate=findViewById(R.id.ivRotate);
        Glide.with(getContext()).load(R.raw.loadder).into(ivRotate);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public RotateDialog(@NonNull Context context) {
        super(context);
    }
}