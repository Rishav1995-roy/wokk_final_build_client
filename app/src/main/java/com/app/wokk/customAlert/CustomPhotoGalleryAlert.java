package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.app.wokk.R;
import java.util.Objects;

public class CustomPhotoGalleryAlert extends Dialog {

    public LinearLayout llGallery;
    public LinearLayout llCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_photo_gallery);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable)(new ColorDrawable(Color.TRANSPARENT)));
        llGallery=findViewById(R.id.llGallery);
        llCamera=findViewById(R.id.llCamera);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public CustomPhotoGalleryAlert(@NonNull Context context) {
        super(context);
    }
}
